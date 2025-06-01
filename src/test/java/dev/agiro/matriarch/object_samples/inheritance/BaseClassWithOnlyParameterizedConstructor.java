package dev.agiro.matriarch.object_samples.inheritance;

public class BaseClassWithOnlyParameterizedConstructor {
    private String name;

    public BaseClassWithOnlyParameterizedConstructor(String name) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
    }

    public String getName() {
        return name;
    }
     public void setName(String name) {
        this.name = name;
    }
}
