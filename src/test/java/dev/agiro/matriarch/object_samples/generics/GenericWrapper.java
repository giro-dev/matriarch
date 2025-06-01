package dev.agiro.matriarch.object_samples.generics;

import dev.agiro.matriarch.object_samples.SimpleRecord; // A concrete, non-generic class

public class GenericWrapper {
    private Box<String> stringBox;
    private Box<Integer> integerBox;
    private Pair<String, SimpleRecord> stringSimpleRecordPair;
    private NumberBox<Double> doubleNumberBox; // Using a concrete type for the bounded generic

    public GenericWrapper() {} // For Matriarch

    // Getters and Setters for all fields
    public Box<String> getStringBox() {
        return stringBox;
    }

    public void setStringBox(Box<String> stringBox) {
        this.stringBox = stringBox;
    }

    public Box<Integer> getIntegerBox() {
        return integerBox;
    }

    public void setIntegerBox(Box<Integer> integerBox) {
        this.integerBox = integerBox;
    }

    public Pair<String, SimpleRecord> getStringSimpleRecordPair() {
        return stringSimpleRecordPair;
    }

    public void setStringSimpleRecordPair(Pair<String, SimpleRecord> stringSimpleRecordPair) {
        this.stringSimpleRecordPair = stringSimpleRecordPair;
    }

    public NumberBox<Double> getDoubleNumberBox() {
        return doubleNumberBox;
    }

    public void setDoubleNumberBox(NumberBox<Double> doubleNumberBox) {
        this.doubleNumberBox = doubleNumberBox;
    }
}
