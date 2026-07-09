package dev.agiro.matriarch.object_samples.edgecases;

public final class ClassWithHiddenProperty {
    private final String maskedValue;

    public ClassWithHiddenProperty(String input) {
        this.maskedValue = "masked-" + (input == null ? "" : input.hashCode());
    }

    public String getMaskedValue() {
        return maskedValue;
    }
}
