package dev.agiro.matriarch.object_samples.edgecases;

public record RecordWithValidation(String name, int score) {
    public RecordWithValidation {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
    }
}
