package dev.agiro.matriarch.object_samples;

import java.time.Instant;

public record SimpleRecord(String name, int value, Instant createdAt) {
}
