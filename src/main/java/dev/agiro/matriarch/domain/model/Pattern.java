package dev.agiro.matriarch.domain.model;


public record Pattern(String coordinate, String value, String type) {
    public Pattern {
        if (type == null) {
            type = "regex";
        }
    }
}
