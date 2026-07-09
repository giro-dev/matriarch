package dev.agiro.matriarch.object_samples.edgecases;

import java.time.Instant;
import java.util.List;

public record RecordWithManyComponents(String a, int b, boolean c, long d, Instant e, List<String> f) {
}
