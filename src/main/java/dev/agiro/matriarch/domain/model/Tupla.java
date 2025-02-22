package dev.agiro.matriarch.domain.model;

public class Tupla<K, V> {
    private final K first;
    private final V second;

    public Tupla(K first, V second) {
        this.first  = first;
        this.second = second;
    }

    public static <K, V> Tupla<K, V> of(K k, V v) {
        return new Tupla<>(k, v);
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }
}
