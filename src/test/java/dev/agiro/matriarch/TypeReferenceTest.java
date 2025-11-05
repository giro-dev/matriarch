package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.domain.model.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for TypeReference and improved generic type handling.
 */
class TypeReferenceTest {

    @Test
    void testTypeReferenceForList() {
        // Test TypeReference with List<String>
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};

        assertNotNull(typeRef.getType());
        assertEquals(List.class, typeRef.getRawType());
        assertEquals(1, typeRef.getActualTypeArguments().length);
    }

    @Test
    void testTypeReferenceForMap() {
        // Test TypeReference with Map<String, Integer>
        TypeReference<Map<String, Integer>> typeRef = new TypeReference<Map<String, Integer>>() {};

        assertNotNull(typeRef.getType());
        assertEquals(Map.class, typeRef.getRawType());
        assertEquals(2, typeRef.getActualTypeArguments().length);
    }

    @Test
    void testTypeReferenceForSet() {
        // Test TypeReference with Set<String>
        TypeReference<Set<String>> typeRef = new TypeReference<Set<String>>() {};

        assertNotNull(typeRef.getType());
        assertEquals(Set.class, typeRef.getRawType());
        assertEquals(1, typeRef.getActualTypeArguments().length);
    }

    @Test
    void testTypeReferenceEquality() {
        // Test TypeReference equality
        TypeReference<List<String>> typeRef1 = new TypeReference<List<String>>() {};
        TypeReference<List<String>> typeRef2 = new TypeReference<List<String>>() {};

        assertEquals(typeRef1, typeRef2);
        assertEquals(typeRef1.hashCode(), typeRef2.hashCode());
    }

    @Test
    void testTypeReferenceToString() {
        // Test TypeReference toString
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};

        String str = typeRef.toString();
        assertNotNull(str);
        assertTrue(str.contains("TypeReference"));
    }

    @Test
    void testMotherWithTypeReference() {
        // Test Mother.forType with TypeReference
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        
        List<String> list = Mother.forType(typeRef).build();

        assertNotNull(list);
        assertTrue(list instanceof List);
    }

    static class Box<T> {
        private T content;

        public T getContent() { return content; }
        public void setContent(T content) { this.content = content; }
    }

    @Test
    void testGenericClassWithTypeReference() {
        // Test generic class with TypeReference
        TypeReference<Box<String>> typeRef = new TypeReference<Box<String>>() {};

        assertEquals(Box.class, typeRef.getRawType());
        assertEquals(1, typeRef.getActualTypeArguments().length);
    }
}

