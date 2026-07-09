package dev.agiro.matriarch.object_samples.edgecases;

public record RecordWithStaticFactory(String name, int score) {
    public static RecordWithStaticFactory create() {
        return new RecordWithStaticFactory("factory", 100);
    }
}
