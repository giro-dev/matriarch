package dev.agiro.matriarch.object_samples.inheritance;

public class SubClassOfParamConstructorBase extends BaseClassWithOnlyParameterizedConstructor {
    private int subValue;

    // This class MUST call super(name) in its constructor.
    // Matriarch needs to handle this.
    public SubClassOfParamConstructorBase(String name, int subValue) {
        super(name); // Matriarch must be able to provide 'name'
        this.subValue = subValue;
    }

    public int getSubValue() {
        return subValue;
    }
    public void setSubValue(int subValue) {
        this.subValue = subValue;
    }
}
