package dev.agiro.matriarch.domain.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TypeReference allows capturing generic type information at runtime.
 * Based on the pattern used by Jackson and similar libraries.
 * Usage:
 * <pre>
 * TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
 * List<String> list = Mother.forType(typeRef).build();
 * </pre>
 */
public abstract class TypeReference<T> {
    
    private final Type type;
    
    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("TypeReference must be constructed with actual type information");
        }
    }
    
    /**
     * Get the captured type.
     */
    public Type getType() {
        return type;
    }
    
    /**
     * Get the raw class for the type.
     */
    @SuppressWarnings("unchecked")
    public Class<T> getRawType() {
        if (type instanceof Class) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        } else {
            throw new IllegalArgumentException("Cannot determine raw type for: " + type);
        }
    }
    
    /**
     * Get the actual type arguments if this is a parameterized type.
     */
    public Type[] getActualTypeArguments() {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return new Type[0];
    }
    
    @Override
    public String toString() {
        return "TypeReference<" + type + ">";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TypeReference<?> other)) return false;
        return type.equals(other.type);
    }
    
    @Override
    public int hashCode() {
        return type.hashCode();
    }
}

