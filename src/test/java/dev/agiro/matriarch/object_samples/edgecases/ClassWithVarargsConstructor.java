package dev.agiro.matriarch.object_samples.edgecases;

import java.util.Arrays;

public class ClassWithVarargsConstructor {
    private final String name;
    private final String[] tags;

    public ClassWithVarargsConstructor(String name, String... tags) {
        this.name = name;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String[] getTags() {
        return tags;
    }

    public int tagCount() {
        return tags == null ? 0 : tags.length;
    }
}
