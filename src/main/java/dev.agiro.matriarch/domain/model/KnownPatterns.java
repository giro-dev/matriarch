package dev.agiro.matriarch.domain.model;


import java.util.ArrayList;
import java.util.List;

public record KnownPatterns(List<Pattern> patterns){

    public KnownPatterns {
        if (patterns == null) {
            patterns = new ArrayList<>();
        }
    }
}
