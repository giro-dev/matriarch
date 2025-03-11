package dev.agiro.matriarch.object_samples;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ContainingParametrizedObjectFields(Map<String, SimpleRecord> map,
                                                 Map<Instant, SimpleRecord> timeMap,
                                                 List<SimpleRecord> list) {
}
