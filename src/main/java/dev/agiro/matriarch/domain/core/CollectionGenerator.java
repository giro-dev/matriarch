package dev.agiro.matriarch.domain.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Handles generation of collections of objects.
 * This follows the Single Responsibility Principle by separating
 * collection generation logic from the Builder.
 *
 * @param <R> the type of objects to generate
 */
public class CollectionGenerator<R> {
    private final Supplier<R> objectSupplier;

    public CollectionGenerator(Supplier<R> objectSupplier) {
        this.objectSupplier = objectSupplier;
    }

    /**
     * Generate multiple objects and return as a List.
     *
     * @param count the number of objects to generate
     * @return a list of generated objects
     */
    public List<R> generateList(int count) {
        validateCount(count);
        List<R> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(objectSupplier.get());
        }
        return result;
    }

    /**
     * Generate multiple objects and return as a Set.
     *
     * @param count the number of objects to generate
     * @return a set of generated objects
     */
    public Set<R> generateSet(int count) {
        validateCount(count);
        Set<R> result = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            result.add(objectSupplier.get());
        }
        return result;
    }

    /**
     * Generate multiple objects and return as a Stream.
     *
     * @param count the number of objects to generate
     * @return a stream of generated objects
     */
    public Stream<R> generateStream(int count) {
        validateCount(count);
        return Stream.generate(objectSupplier).limit(count);
    }

    /**
     * Generate an infinite Stream of objects.
     *
     * @return an infinite stream of generated objects
     */
    public Stream<R> generateInfiniteStream() {
        return Stream.generate(objectSupplier);
    }

    private void validateCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count must be non-negative: " + count);
        }
    }
}

