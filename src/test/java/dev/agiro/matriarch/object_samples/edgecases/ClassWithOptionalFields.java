package dev.agiro.matriarch.object_samples.edgecases;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class ClassWithOptionalFields {
    private Optional<String> optionalString;
    private OptionalInt optionalInt;
    private OptionalLong optionalLong;
    private OptionalDouble optionalDouble;

    public Optional<String> getOptionalString() {
        return optionalString;
    }

    public OptionalInt getOptionalInt() {
        return optionalInt;
    }

    public OptionalLong getOptionalLong() {
        return optionalLong;
    }

    public OptionalDouble getOptionalDouble() {
        return optionalDouble;
    }
}
