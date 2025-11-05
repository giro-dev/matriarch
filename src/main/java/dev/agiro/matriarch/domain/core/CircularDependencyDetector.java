package dev.agiro.matriarch.domain.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Detects circular dependencies during object generation.
 * Uses ThreadLocal to maintain a stack of classes being generated per thread.
 */
public class CircularDependencyDetector {
    
    private static final CircularDependencyDetector INSTANCE = new CircularDependencyDetector();
    
    private final ThreadLocal<Set<Class<?>>> generationStack = ThreadLocal.withInitial(HashSet::new);
    private final ThreadLocal<Integer> depthCounter = ThreadLocal.withInitial(() -> 0);
    
    private static final int MAX_DEPTH = 50; // Maximum nesting depth
    
    private CircularDependencyDetector() {
        // Private constructor for singleton
    }
    
    public static CircularDependencyDetector getInstance() {
        return INSTANCE;
    }
    
    /**
     * Check if we're currently generating an instance of the given class.
     * This indicates a circular dependency.
     */
    public boolean isCircular(Class<?> clazz) {
        return generationStack.get().contains(clazz);
    }
    
    /**
     * Check if we've exceeded the maximum nesting depth.
     */
    public boolean isMaxDepthExceeded() {
        return depthCounter.get() >= MAX_DEPTH;
    }
    
    /**
     * Get the current nesting depth.
     */
    public int getDepth() {
        return depthCounter.get();
    }
    
    /**
     * Push a class onto the generation stack and increment depth.
     */
    public void push(Class<?> clazz) {
        generationStack.get().add(clazz);
        depthCounter.set(depthCounter.get() + 1);
    }
    
    /**
     * Pop a class from the generation stack and decrement depth.
     */
    public void pop(Class<?> clazz) {
        generationStack.get().remove(clazz);
        depthCounter.set(Math.max(0, depthCounter.get() - 1));
    }
    
    /**
     * Clear the generation stack for the current thread.
     * Should be called after completing a top-level generation.
     */
    public void clear() {
        generationStack.get().clear();
        depthCounter.set(0);
    }
    
    /**
     * Execute a generation operation with circular dependency detection.
     * Returns null if circular dependency is detected instead of throwing an exception.
     */
    public <T> T executeWithDetection(Class<?> clazz, GenerationOperation<T> operation) {
        if (isCircular(clazz)) {
            return null; // Return null to break the cycle
        }
        
        if (isMaxDepthExceeded()) {
            return null; // Prevent stack overflow
        }
        
        push(clazz);
        try {
            return operation.generate();
        } finally {
            pop(clazz);
        }
    }
    
    /**
     * Get a copy of the current generation stack (for debugging).
     */
    public Set<Class<?>> getCurrentStack() {
        return new HashSet<>(generationStack.get());
    }
    
    @FunctionalInterface
    public interface GenerationOperation<T> {
        T generate();
    }
}

