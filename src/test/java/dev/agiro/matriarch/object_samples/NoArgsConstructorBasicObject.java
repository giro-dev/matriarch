package dev.agiro.matriarch.object_samples;




public class NoArgsConstructorBasicObject {
    private String string;
    private int    integer;
    private int    knownInteger;

    public NoArgsConstructorBasicObject() {
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public int getKnownInteger() {
        return knownInteger;
    }
    public void setKnownInteger(int knownInteger) {
        this.knownInteger = knownInteger;
    }

    @Override
    public String toString() {
        return "NoArgsConstructorBasicObject{" +
                "string='" + string + '\'' +
                ", integer=" + integer +
                ", knownInteger=" + knownInteger +
                '}';
    }
}