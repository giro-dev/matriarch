package dev.agiro.matriarch.suppliers.base;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Abstract base class for suppliers that need random number generation.
 * Provides a thread-safe Random instance for subclasses.
 */
public abstract class RandomSupplier<T> implements Supplier<T> {

    protected static final Random RANDOM = new Random();

    /**
     * Selects a random element from the provided array.
     *
     * @param items the array to select from
     * @param <E>   the element type
     * @return a randomly selected element
     */
    protected <E> E randomElement(E[] items) {
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        return items[RANDOM.nextInt(items.length)];
    }

    /**
     * Generates a random integer between min (inclusive) and max (exclusive).
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a random integer in the specified range
     */
    protected int randomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        return RANDOM.nextInt(max - min) + min;
    }
}

