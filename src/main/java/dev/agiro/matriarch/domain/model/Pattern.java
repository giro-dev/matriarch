package dev.agiro.matriarch.domain.model;

import java.util.Objects;

public class Pattern {
    private String coordinate;
    private String value;
    private String type;

    public Pattern() {
        // Default type to "regex" if not otherwise set,
        // mimicking the record's compact constructor logic.
        this.type = "regex";
    }

    public Pattern(String coordinate, String value, String type) {
        this.coordinate = coordinate;
        this.value = value;
        if (type == null) {
            this.type = "regex";
        } else {
            this.type = type;
        }
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null) {
            this.type = "regex";
        } else {
            this.type = type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern = (Pattern) o;
        return Objects.equals(coordinate, pattern.coordinate) &&
                Objects.equals(value, pattern.value) &&
                Objects.equals(type, pattern.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, value, type);
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "coordinate='" + coordinate + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
