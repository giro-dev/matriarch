package dev.agiro.matriarch.object_samples.records;

// Reusing SimpleRecord from the parent package for simplicity, adjust if needed.
import dev.agiro.matriarch.object_samples.SimpleRecord;

public record NestedRecordOuter(String outerField, SimpleRecord nested) {
}
