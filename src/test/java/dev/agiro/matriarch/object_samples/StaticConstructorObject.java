package dev.agiro.matriarch.object_samples;


public record StaticConstructorObject (AllArgsConstructorBasicObjectAllTypes value){
    public static StaticConstructorObject with(AllArgsConstructorBasicObjectAllTypes value) {
        return new StaticConstructorObject(value);
    }

}
