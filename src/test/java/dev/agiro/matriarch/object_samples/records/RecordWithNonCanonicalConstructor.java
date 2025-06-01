package dev.agiro.matriarch.object_samples.records;

public record RecordWithNonCanonicalConstructor(String id, String description) {
    // Canonical constructor is implicit

    // Additional non-canonical constructor
    public RecordWithNonCanonicalConstructor(String id) {
        this(id, "Default Description");
    }
}
