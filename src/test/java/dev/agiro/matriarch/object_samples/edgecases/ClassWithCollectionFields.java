package dev.agiro.matriarch.object_samples.edgecases;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassWithCollectionFields {
    private List<String> items;
    private Set<Integer> numbers;
    private Map<String, Integer> mappings;

    public List<String> getItems() {
        return items;
    }

    public Set<Integer> getNumbers() {
        return numbers;
    }

    public Map<String, Integer> getMappings() {
        return mappings;
    }
}
