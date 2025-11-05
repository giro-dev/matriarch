package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for circular dependency detection.
 */
class CircularDependencyTest {

    static class Node {
        private String value;
        private Node next;
        private Node parent;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        
        public Node getNext() { return next; }
        public void setNext(Node next) { this.next = next; }
        
        public Node getParent() { return parent; }
        public void setParent(Node parent) { this.parent = parent; }
    }

    static class A {
        private String name;
        private B b;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public B getB() { return b; }
        public void setB(B b) { this.b = b; }
    }

    static class B {
        private String value;
        private A a;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        
        public A getA() { return a; }
        public void setA(A a) { this.a = a; }
    }

    @Test
    void testSelfReferentialClass() {
        // Test that self-referential classes don't cause infinite loops
        Node node = Mother.forClass(Node.class).build();

        assertNotNull(node);
        assertNotNull(node.getValue());
        // next and parent should be null to break the cycle
        assertNull(node.getNext(), "Self-reference should be null to break cycle");
    }

    @Test
    void testMutuallyReferentialClasses() {
        // Test that mutually referential classes don't cause infinite loops
        A a = Mother.forClass(A.class).build();

        assertNotNull(a);
        assertNotNull(a.getName());
        assertNotNull(a.getB());
        // The circular reference should be broken at some point
        // b.a should be null to prevent infinite recursion
        if (a.getB() != null) {
            assertNull(a.getB().getA(), "Circular reference should be null to break cycle");
        }
    }

    @Test
    void testDeepNesting() {
        // Test that deep nesting doesn't cause stack overflow
        class Container {
            private Container nested;
            private String value;

            public Container getNested() { return nested; }
            public void setNested(Container nested) { this.nested = nested; }
            
            public String getValue() { return value; }
            public void setValue(String value) { this.value = value; }
        }

        Container container = Mother.forClass(Container.class).build();

        assertNotNull(container);
        assertNotNull(container.getValue());
        // Should handle deep nesting without stack overflow
    }
}

