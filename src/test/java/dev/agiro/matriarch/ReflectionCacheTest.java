package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.ReflectionCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ReflectionCache performance optimization.
 */
class ReflectionCacheTest {

    static class TestClass {
        private String field1;
        private int field2;
        
        public TestClass() {}
        
        public TestClass(String field1) {
            this.field1 = field1;
        }
        
        public void setField1(String field1) {
            this.field1 = field1;
        }
        
        public static TestClass create() {
            return new TestClass();
        }
    }

    @AfterEach
    void cleanup() {
        // Clean cache after each test
        ReflectionCache.getInstance().clearAll();
    }

    @Test
    void testGetFieldsCaching() {
        ReflectionCache cache = ReflectionCache.getInstance();

        // First call - should cache the result
        List<Field> fields1 = cache.getFields(TestClass.class);
        assertNotNull(fields1);
        assertEquals(2, fields1.size());

        // Second call - should return cached result
        List<Field> fields2 = cache.getFields(TestClass.class);
        assertSame(fields1, fields2, "Should return the same cached instance");
    }

    @Test
    void testGetConstructorsCaching() {
        ReflectionCache cache = ReflectionCache.getInstance();

        // First call - should cache the result
        List<Constructor<?>> constructors1 = cache.getConstructors(TestClass.class);
        assertNotNull(constructors1);
        assertEquals(2, constructors1.size());

        // Second call - should return cached result
        List<Constructor<?>> constructors2 = cache.getConstructors(TestClass.class);
        assertSame(constructors1, constructors2, "Should return the same cached instance");
    }

    @Test
    void testGetStaticFactoryMethodsCaching() {
        ReflectionCache cache = ReflectionCache.getInstance();

        // First call - should cache the result
        List<Method> methods1 = cache.getStaticFactoryMethods(TestClass.class);
        assertNotNull(methods1);
        assertEquals(1, methods1.size());
        assertEquals("create", methods1.get(0).getName());

        // Second call - should return cached result
        List<Method> methods2 = cache.getStaticFactoryMethods(TestClass.class);
        assertSame(methods1, methods2, "Should return the same cached instance");
    }

    @Test
    void testGetSetterMethodCaching() {
        ReflectionCache cache = ReflectionCache.getInstance();

        // First call - should cache the result
        var setter1 = cache.getSetterMethod(TestClass.class, "field1");
        assertTrue(setter1.isPresent());
        assertEquals("setField1", setter1.get().getName());

        // Second call - should return cached result
        var setter2 = cache.getSetterMethod(TestClass.class, "field1");
        assertTrue(setter2.isPresent());
        assertSame(setter1.get(), setter2.get(), "Should return the same cached method");
    }

    @Test
    void testClearAll() {
        ReflectionCache cache = ReflectionCache.getInstance();

        // Cache some data
        cache.getFields(TestClass.class);
        cache.getConstructors(TestClass.class);

        // Clear all
        cache.clearAll();

        // After clearing, should recache (can't easily test this directly,
        // but we can verify the cache still works)
        List<Field> fields = cache.getFields(TestClass.class);
        assertNotNull(fields);
    }

    @Test
    void testClearForClass() {
        ReflectionCache cache = ReflectionCache.getInstance();

        // Cache data for TestClass
        List<Field> fields1 = cache.getFields(TestClass.class);
        
        // Clear cache for TestClass
        cache.clearForClass(TestClass.class);

        // Should recache on next access
        List<Field> fields2 = cache.getFields(TestClass.class);
        assertNotNull(fields2);
        // New instance since cache was cleared
        assertNotSame(fields1, fields2);
    }

    @Test
    void testInheritedFieldsCaching() {
        class Parent {
            private String parentField;
        }
        
        class Child extends Parent {
            private String childField;
        }

        ReflectionCache cache = ReflectionCache.getInstance();
        
        List<Field> fields = cache.getFields(Child.class);
        assertNotNull(fields);
        assertEquals(2, fields.size(), "Should include inherited fields");
        
        // Verify caching works for inheritance
        List<Field> fields2 = cache.getFields(Child.class);
        assertSame(fields, fields2);
    }
}

