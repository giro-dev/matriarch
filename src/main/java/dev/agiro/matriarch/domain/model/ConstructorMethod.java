package dev.agiro.matriarch.domain.model;


public class ConstructorMethod<T> {

    private final T            t;
    private final InstanceType instanceType;

    public ConstructorMethod(T t, InstanceType instanceType) {
        this.t            = t;
        this.instanceType = instanceType;
    }

    public static <T> ConstructorMethod<T> of(T t, InstanceType instanceType) {
        return new ConstructorMethod<>(t, instanceType);
    }

    public T getInstance() {
        return t;
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }
}
