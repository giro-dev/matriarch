package dev.agiro.matriarch;

import dev.agiro.matriarch.application.Mother;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MotherTest {
    @Test
    void random_object() {
        TestClass build = Mother.forObject(TestClass.class).create();
        assertAll("Non null values",
                () -> assertNotNull(build.getIntObject()),
                () -> assertNotNull(build.getString()),
                () -> assertNotNull(build.getNestedObject()),
                () -> assertNotNull(build.getNestedObject().getInstant()));
    }


    public static class TestClass {
        String string;

        public TestClass() {
        }

        public TestClass(String string, int intiger, Integer intObject, NestedObject nestedObject) {
            this.string = string;
            this.intiger = intiger;
            this.intObject = intObject;
            this.nestedObject = nestedObject;
        }

        int intiger;
        Integer intObject;
        NestedObject nestedObject;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public int getIntiger() {
            return intiger;
        }

        public void setIntiger(int intiger) {
            this.intiger = intiger;
        }

        public Integer getIntObject() {
            return intObject;
        }

        public void setIntObject(Integer intObject) {
            this.intObject = intObject;
        }

        public NestedObject getNestedObject() {
            return nestedObject;
        }

        public void setNestedObject(NestedObject nestedObject) {
            this.nestedObject = nestedObject;
        }
    }

    public static class NestedObject{
            Instant instant;

        public NestedObject() {
        }

        public NestedObject(Instant instant) {
            this.instant = instant;
        }

        public Instant getInstant() {
            return instant;
        }

        public void setInstant(Instant instant) {
            this.instant = instant;
        }
    }

}