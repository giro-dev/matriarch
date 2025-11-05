package dev.agiro.matriarch.domain.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for reflection operations to improve performance.
 * Thread-safe implementation using ConcurrentHashMap.
 */
public class ReflectionCache {
    
    private static final ReflectionCache INSTANCE = new ReflectionCache();
    
    private final Map<Class<?>, List<Field>> fieldsCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Constructor<?>>> constructorsCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Method>> staticFactoryMethodsCache = new ConcurrentHashMap<>();
    private final Map<String, Method> setterMethodsCache = new ConcurrentHashMap<>();
    
    private ReflectionCache() {
        // Private constructor for singleton
    }
    
    public static ReflectionCache getInstance() {
        return INSTANCE;
    }
    
    /**
     * Get all non-static fields for a class, including inherited fields.
     * Results are cached for performance.
     */
    public List<Field> getFields(Class<?> clazz) {
        return fieldsCache.computeIfAbsent(clazz, this::extractFields);
    }
    
    private List<Field> extractFields(Class<?> clazz) {
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            Arrays.stream(currentClass.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .forEach(allFields::add);
            currentClass = currentClass.getSuperclass();
        }
        return Collections.unmodifiableList(allFields);
    }
    
    /**
     * Get all constructors for a class.
     * Results are cached for performance.
     */
    public List<Constructor<?>> getConstructors(Class<?> clazz) {
        return constructorsCache.computeIfAbsent(clazz, this::extractConstructors);
    }
    
    private List<Constructor<?>> extractConstructors(Class<?> clazz) {
        return Collections.unmodifiableList(Arrays.asList(clazz.getDeclaredConstructors()));
    }
    
    /**
     * Get all public static factory methods for a class that return an instance of that class.
     * Results are cached for performance.
     */
    public List<Method> getStaticFactoryMethods(Class<?> clazz) {
        return staticFactoryMethodsCache.computeIfAbsent(clazz, this::extractStaticFactoryMethods);
    }
    
    private List<Method> extractStaticFactoryMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()) &&
                                Modifier.isPublic(method.getModifiers()) &&
                                clazz.isAssignableFrom(method.getReturnType()))
                .toList();
    }
    
    /**
     * Get a setter method for a field in a class.
     * Results are cached for performance.
     */
    public Optional<Method> getSetterMethod(Class<?> clazz, String fieldName) {
        String cacheKey = clazz.getName() + "#" + fieldName;
        Method cachedMethod = setterMethodsCache.get(cacheKey);
        
        if (cachedMethod != null) {
            return Optional.of(cachedMethod);
        }
        
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Optional<Method> setter = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals(setterName))
                .findFirst();
        
        setter.ifPresent(method -> setterMethodsCache.put(cacheKey, method));
        
        return setter;
    }
    
    /**
     * Clear all caches. Useful for testing or memory management.
     */
    public void clearAll() {
        fieldsCache.clear();
        constructorsCache.clear();
        staticFactoryMethodsCache.clear();
        setterMethodsCache.clear();
    }
    
    /**
     * Clear cache for a specific class.
     */
    public void clearForClass(Class<?> clazz) {
        fieldsCache.remove(clazz);
        constructorsCache.remove(clazz);
        staticFactoryMethodsCache.remove(clazz);
        // Clear setter methods for this class
        setterMethodsCache.keySet().removeIf(key -> key.startsWith(clazz.getName() + "#"));
    }
}

