package dev.agiro.matriarch.object_samples;

public class FieldsVisibility {
    public String publicField;
    private String privateField;
    protected String protectedField;
    String packagePrivateField; // Default visibility

    // Getter for private field to allow assertion
    public String getPrivateField() {
        return privateField;
    }

    // Default constructor for Matriarch to instantiate
    public FieldsVisibility() {}
}
