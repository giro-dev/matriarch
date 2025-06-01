package dev.agiro.matriarch.object_samples;

public class SimplePackagePrivateConstructor {
    private double amount;

    SimplePackagePrivateConstructor(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
