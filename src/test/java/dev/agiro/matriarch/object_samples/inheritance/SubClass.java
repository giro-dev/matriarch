package dev.agiro.matriarch.object_samples.inheritance;

public class SubClass extends BaseClass {
    private boolean subFlag;
    public String subOnlyField;

    public SubClass() {} // Ensure there's a constructor Matriarch can use

    public SubClass(String baseField, int baseNumber, boolean subFlag, String subOnlyField) {
        super(baseField, baseNumber);
        this.subFlag = subFlag;
        this.subOnlyField = subOnlyField;
    }

    public boolean isSubFlag() {
        return subFlag;
    }

    public String getSubOnlyField() {
        return subOnlyField;
    }

    public void setSubFlag(boolean subFlag) {
        this.subFlag = subFlag;
    }

    public void setSubOnlyField(String subOnlyField) {
        this.subOnlyField = subOnlyField;
    }
}
