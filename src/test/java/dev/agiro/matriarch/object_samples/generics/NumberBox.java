package dev.agiro.matriarch.object_samples.generics;

// Assuming SimpleRecord is available for use as a Number provider if needed, or basic Numbers.
// import dev.agiro.matriarch.object_samples.SimpleRecord;

public class NumberBox<T extends Number> {
    private T numberContent;
    // private SimpleRecord otherField; // Example of a non-generic field

    public NumberBox() {} // For Matriarch

    public NumberBox(T numberContent) {
        this.numberContent = numberContent;
    }

    public T getNumberContent() {
        return numberContent;
    }

    public void setNumberContent(T numberContent) {
        this.numberContent = numberContent;
    }

    // public SimpleRecord getOtherField() { return otherField; }
    // public void setOtherField(SimpleRecord otherField) { this.otherField = otherField; }

}
