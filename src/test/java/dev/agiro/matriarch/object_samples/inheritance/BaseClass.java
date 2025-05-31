package dev.agiro.matriarch.object_samples.inheritance;

public class BaseClass {
    private String baseField;
    protected int baseNumber;

    public BaseClass() {} // Ensure there's a constructor Matriarch can use

    public BaseClass(String baseField, int baseNumber) {
        this.baseField = baseField;
        this.baseNumber = baseNumber;
    }

    public String getBaseField() {
        return baseField;
    }

    public int getBaseNumber() {
        return baseNumber;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }

    public void setBaseNumber(int baseNumber) {
        this.baseNumber = baseNumber;
    }
}
