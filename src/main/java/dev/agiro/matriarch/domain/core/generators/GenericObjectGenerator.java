package dev.agiro.matriarch.domain.core.generators;

import dev.agiro.matriarch.domain.core.CircularDependencyDetector;
import dev.agiro.matriarch.domain.core.ReflectionCache;
import dev.agiro.matriarch.domain.model.*;
import dev.agiro.matriarch.exception.MatriarchInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GenericObjectGenerator extends AbstractGenerator<Object> implements MultiGenerator {

    Logger log = Logger.getLogger(GenericObjectGenerator.class.getName());

    private final Map<ClazzGenerators, AbstractGenerator<?>> generators;
    private final ReflectionCache reflectionCache = ReflectionCache.getInstance();
    private final CircularDependencyDetector circularDetector = CircularDependencyDetector.getInstance();

    public GenericObjectGenerator(Map<ClazzGenerators, AbstractGenerator<?>> generators) {
        super(Object.class);
        this.generators = generators;
    }

    @Override
    public Object generate(Definition classDefinition) {
        final var overrideValues     = classDefinition.overrideValues();
        final var overrideCoordinate = classDefinition.overrideCoordinate();
        final Class<?> clazz = classDefinition.clazz();

        // Check for circular dependencies
        if (circularDetector.isCircular(clazz)) {
            log.fine(() -> "Circular dependency detected for class " + clazz.getName() + " at coordinate " + overrideCoordinate);
            return null; // Break the cycle by returning null
        }

        if (circularDetector.isMaxDepthExceeded()) {
            log.warning(() -> "Maximum nesting depth exceeded at coordinate " + overrideCoordinate);
            return null; // Prevent stack overflow
        }

        // Execute generation with circular dependency tracking
        return circularDetector.executeWithDetection(clazz, () -> {
            if (overrideValues.containsKey(overrideCoordinate)
                && Overrider.OverriderType.OBJECT.equals(overrideValues.get(overrideCoordinate).type())) {
                return secureCast(clazz,
                                  overrideValues.get(overrideCoordinate).value());
            }

            final var resolvedTypesForInstance = classDefinition.getResolvedGenericTypeMap();
            final var instance = getInstance(classDefinition, resolvedTypesForInstance);
            if (instance.getInstance() == null) {
                return null;
            }

            // This map will hold the resolution of the current instance's own type parameters.
            // For example, if instance is Box<String>, this map becomes {T -> String}.
            Map<TypeVariable<?>, Type> instanceSpecificTypeMap = new HashMap<>();
            Class<?> instanceClass = instance.getInstance().getClass(); // e.g., Box.class
            TypeVariable<?>[] typeParameters = instanceClass.getTypeParameters(); // [T for Box]

            // actualTypes are from the Definition of the field/parameter that *led* to this GenericObjectGenerator call
            // e.g. if a field was `Box<String> myBox`, classDefinition.parametrizedType() would be `[String.class]`
            Type[] actualTypeArguments = classDefinition.parametrizedType();

            if (typeParameters.length > 0 && actualTypeArguments.length > 0 && typeParameters.length == actualTypeArguments.length) {
                for (int i = 0; i < typeParameters.length; i++) {
                    instanceSpecificTypeMap.put(typeParameters[i], actualTypeArguments[i]);
                }
            }
            // Important: Merge with types resolved from an outer context, if any.
            // Outer context takes precedence if there's a name clash, though typically type var names are unique per class.
            instanceSpecificTypeMap.putAll(resolvedTypesForInstance);


            final Object finalInstance = instance.getInstance();
            final InstanceType creationType = instance.getInstanceType(); // Get how it was created

            resolveFields(finalInstance.getClass())
                .forEach(field -> {
                    // Construct the full path for the current field to check against overrideValues
                    String fieldOverrideKey = (overrideCoordinate.isEmpty() ? // Use overrideCoordinate from classDefinition
                                              field.getName() :
                                              overrideCoordinate + "." + field.getName());

                    if (creationType != InstanceType.STATIC_METHOD || overrideValues.containsKey(fieldOverrideKey)) {
                        // If not created by static factory OR if there's an explicit override for this field
                        setValueToField(finalInstance,
                                        field,
                                        overrideValues,
                                        overrideCoordinate, // base coordinate for current object
                                        instanceSpecificTypeMap);
                    }
                    // Else: (created by static factory AND no explicit override for this field) -> do nothing, preserve factory value.
                });
            return finalInstance;
        });
    }

    private void setValueToField(Object object,
                                 Field field,
                                 Map<String, Overrider> overrideValues,
                                 String currentField,
                                 Map<TypeVariable<?>, Type> resolvedGenericTypeMapFromParent) { // Renamed for clarity

        try {
            if (!Modifier.isFinal(field.getModifiers())) {

                String fieldName = currentField.isEmpty() ? field.getName() : currentField + "." + field.getName();

                field.setAccessible(true);
                // Pass the received map to FieldDefinition
                field.set(object, generateValue(new FieldDefinition(field,
                                                                    overrideValues,
                                                                    fieldName,
                                                                    resolvedGenericTypeMapFromParent)));
            }
        } catch (Exception e) {
            // Use reflection cache for setter method lookup
            final var setter = reflectionCache.getSetterMethod(object.getClass(), field.getName());

            if (setter.isPresent()) {
                try {
                    setter.get().invoke(object, generateValue(new FieldDefinition(field,
                                                                                  overrideValues,
                                                                                  currentField.isEmpty() ? field.getName() : currentField + "." + field.getName(),
                                                                                  resolvedGenericTypeMapFromParent)));
                } catch (Exception ex) {
                    log.finest(() -> "ObjectMother not able to set field %s in class %s nor reflection or the method %s. %s".formatted(
                            field.getName(),
                            object.getClass().getName(),
                            setter.get().getName(),
                            ex.getMessage()));
                }
            }
            log.severe(() -> "ObjectMother not able to set field %s in class %s with value %s".formatted(
                    field.getName(),
                    object.getClass().getName(),
                    e.getMessage()));
        }
    }

    private List<Field> resolveFields(Class<?> aClass) {
        return reflectionCache.getFields(aClass);
    }

    // This map is for resolving type variables that might appear in constructor parameters,
    // based on the context where this class (clazz) is being instantiated.
    private ConstructorMethod<?> getInstance(Definition classDefinition, Map<TypeVariable<?>, Type> contextResolvedTypeMap) {
        final Class<?> clazz = classDefinition.clazz();
        final String overrideCoordinate = classDefinition.overrideCoordinate();

        // Attempt to find and use a suitable public static factory method first
        // Use reflection cache for better performance
        List<java.lang.reflect.Method> staticFactoryMethods = reflectionCache.getStaticFactoryMethods(clazz);

        // Prioritize no-arg static factory methods
        java.lang.reflect.Method noArgStaticFactory = staticFactoryMethods.stream()
            .filter(method -> method.getParameterCount() == 0)
            .findFirst()
            .orElse(null);

        if (noArgStaticFactory != null) {
            try {
                noArgStaticFactory.setAccessible(true);
                Object instance = noArgStaticFactory.invoke(null);
                return ConstructorMethod.of(secureCast(clazz, instance), InstanceType.STATIC_METHOD);
            } catch (Exception e) {
                log.finest(() -> "Error invoking no-arg static factory method %s for class %s: %s".formatted(noArgStaticFactory.getName(), clazz.getName(), e.getMessage()));
                // Fall through to constructor logic if static factory invocation fails
            }
        }

        // TODO: Extend to handle static factory methods with parameters Matriarch can satisfy.

        // Fallback to constructor logic
        // Use reflection cache for better performance
        final Map<List<Parameter>, Constructor<?>> constructors = reflectionCache.getConstructors(clazz).stream()
                .collect(Collectors.toMap(
                        c -> List.of(c.getParameters()),
                        c -> c));
        AtomicReference<Constructor<?>> constructorRef = new AtomicReference<>();
        try {
            if (constructors.isEmpty()) {
                // This case might be hit if only a static factory was present and failed, or no public constructors.
                // Or if no public static factory methods and no public constructors.
                String message = "No suitable public static factory method or public constructor found for class %s".formatted(clazz.getCanonicalName());
                log.severe(message);
                throw new MatriarchInstantiationException(message);
            }

            // Try to find a public constructor first

            // Try to find a public constructor first
            java.util.Optional<Constructor<?>> chosenConstructorOpt = constructors.entrySet().stream()
                                    .filter(entry -> Modifier.isPublic(entry.getValue().getModifiers()))
                                    .min(java.util.Comparator.comparingInt(entry -> entry.getKey().size()))
                                    .map(Map.Entry::getValue);

            if (chosenConstructorOpt.isEmpty()) {
                // If no public constructor, try protected or package-private (non-private, non-public)
                chosenConstructorOpt = constructors.entrySet().stream()
                    .filter(entry -> !Modifier.isPrivate(entry.getValue().getModifiers()) && !Modifier.isPublic(entry.getValue().getModifiers()))
                    .min(java.util.Comparator.comparingInt(entry -> entry.getKey().size()))
                    .map(Map.Entry::getValue);

                if (chosenConstructorOpt.isPresent()) {
                    constructorRef.set(chosenConstructorOpt.get());
                    constructorRef.get().setAccessible(true); // Make protected/package-private accessible
                } else {
                    // If only private constructors are left (or no constructors at all after static factory check)
                    // This path will be taken for SimplePrivateConstructor.
                    throw new MatriarchInstantiationException(
                        "No suitable public, protected, or package-private constructor found, and no suitable public static factory method for class " + clazz.getName());
                }
            } else { // Public constructor was found
                 constructorRef.set(chosenConstructorOpt.get());
                 // No need to setAccessible for public constructor
            }

            Constructor<?> constructor = constructorRef.get();
            // Note: setAccessible has been called for non-public (but not private) constructors if chosen.
            // Public constructors don't need it. Private constructors would have led to an exception above.

            var type = constructor.getParameterCount() == 0 ? InstanceType.NO_ARGS_CONSTRUCTOR : InstanceType.CONSTRUCTOR;
            final Object[] parameters = Arrays.stream(constructor.getParameters())
                    .map(parameter -> generateValue(new ParameterDefinition(parameter,
                                                                            classDefinition.overrideValues(),
                                                                            overrideCoordinate.isEmpty() ? parameter.getName() :
                                                                                    overrideCoordinate + "." + parameter.getName(),
                                                                            contextResolvedTypeMap))) // Pass map for constructor parameters
                    .toArray();
            return ConstructorMethod.of(secureCast(clazz, constructor.newInstance(parameters)), type);
        } catch (Exception e) {
            log.finest(() -> String.format("Error instantiating class %s with constructor %s: %s",
                                           clazz,
                                           constructorRef.get() == null ? "not constructor found" : Arrays.stream(
                                                           constructorRef.get().getParameters())
                                                   .map(parameter -> parameter.getType().getSimpleName() + " " + parameter.getName())
                                                   .collect(Collectors.joining(",  ", "(", ")")),
                                           e.getMessage()));
            log.severe(() -> String.format("Error instantiating class %s with constructor. field %s will be null: %s. Error: %s",
                                           clazz,
                                           overrideCoordinate,
                                           e.getMessage(),
                                           e.getClass().getCanonicalName()));
            throw new MatriarchInstantiationException("Failed to instantiate " + clazz.getName() + " using constructor " + constructorRef.get() + ": " + e.getMessage(), e);
        }
    }

    private Object generateValue(Definition definition) {
        // Use the overloaded forClass method that considers parameterized types
        final var generator = generators.get(ClazzGenerators.forClass(definition.clazz(), definition.parametrizedType()));
        return generator.apply(definition);
    }

    private <T> T secureCast(Class<T> tClass, Object value) {
        if (value == null) return null;
        if (tClass.isInstance(value)) {
            return tClass.cast(value);
        } else {
            throw new RuntimeException("Unable to parse object of type %s to %s (%s)".formatted(value.getClass().getCanonicalName(),
                                                                                                tClass.getCanonicalName()
                    , value.toString()));
        }
    }


    @Override
    public Map<ClazzGenerators, AbstractGenerator<?>> getGenerator() {
        return generators;
    }
}
