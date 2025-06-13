package dev.agiro.matriarch.domain.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KnownPatterns {
    private List<Pattern> patterns = new ArrayList<>();

    public KnownPatterns() {}


    public KnownPatterns(List<Pattern> patterns) {
        if (patterns == null) {
            patterns = new ArrayList<>();
        }
        this.patterns = patterns;
    }

    public List<Pattern> patterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (KnownPatterns) obj;
        return Objects.equals(this.patterns, that.patterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patterns);
    }

    @Override
    public String toString() {
        return "KnownPatterns[" +
                "patterns=" + patterns + ']';
    }

}
