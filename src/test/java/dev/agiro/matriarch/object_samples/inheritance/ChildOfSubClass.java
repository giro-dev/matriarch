package dev.agiro.matriarch.object_samples.inheritance;

public class ChildOfSubClass extends SubClass {
    private double childDouble;

    public ChildOfSubClass() {} // Ensure there's a constructor Matriarch can use

    public ChildOfSubClass(String baseField, int baseNumber, boolean subFlag, String subOnlyField, double childDouble) {
        super(baseField, baseNumber, subFlag, subOnlyField);
        this.childDouble = childDouble;
    }

    public double getChildDouble() {
        return childDouble;
    }

    public void setChildDouble(double childDouble) {
        this.childDouble = childDouble;
    }
}
