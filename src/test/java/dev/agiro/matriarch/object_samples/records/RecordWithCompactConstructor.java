package dev.agiro.matriarch.object_samples.records;

public record RecordWithCompactConstructor(String item, double price) {
    public RecordWithCompactConstructor {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        item = item.trim();
    }
}
