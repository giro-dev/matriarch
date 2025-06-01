package dev.agiro.matriarch.object_samples.records;

import java.util.List;
import java.util.Map;
import java.util.Set;
import dev.agiro.matriarch.object_samples.SimpleRecord;

public record RecordWithCollections(
        List<String> stringList,
        Set<Integer> integerSet,
        Map<String, SimpleRecord> recordMap) {
}
