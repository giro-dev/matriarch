package dev.agiro.matriarch.object_samples.edgecases;

public final class FinalClassWithAllFinalFields {
    private final String label;
    private final long amount;

    public FinalClassWithAllFinalFields(String label, long amount) {
        this.label = label;
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public long getAmount() {
        return amount;
    }
}
