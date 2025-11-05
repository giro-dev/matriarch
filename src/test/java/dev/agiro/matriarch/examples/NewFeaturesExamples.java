package dev.agiro.matriarch.examples;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.domain.model.TypeReference;

import java.time.LocalDate;
import java.util.List;

/**
 * Comprehensive examples demonstrating the new features in Matriarch v0.4.3+
 */
public class NewFeaturesExamples {

    // Example domain classes
    static class User {
        private String email;
        private String password;
        private String internalId;
        private LocalDate birthDate;
        private int age;
        private boolean active;
        private List<String> roles;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getInternalId() { return internalId; }
        public void setInternalId(String internalId) { this.internalId = internalId; }
        
        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
        
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }

        @Override
        public String toString() {
            return "User{" +
                    "email='" + email + '\'' +
                    ", password='" + (password != null ? "***" : "null") + '\'' +
                    ", internalId='" + internalId + '\'' +
                    ", birthDate=" + birthDate +
                    ", age=" + age +
                    ", active=" + active +
                    ", roles=" + roles +
                    '}';
        }
    }

    static class Node {
        private String value;
        private Node next;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        
        public Node getNext() { return next; }
        public void setNext(Node next) { this.next = next; }

        @Override
        public String toString() {
            return "Node{value='" + value + "', next=" + (next != null ? "Node{...}" : "null") + "}";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Matriarch New Features Examples ===\n");

        // Example 1: Basic usage (still works as before)
        example1_BasicUsage();

        // Example 2: Enhanced Builder - Exclude Fields
        example2_ExcludeFields();

        // Example 3: Enhanced Builder - Custom Field Generators
        example3_CustomFieldGenerators();

        // Example 4: Enhanced Builder - Combined Features
        example4_CombinedFeatures();

        // Example 5: TypeReference for Generic Types
        example5_TypeReference();

        // Example 6: Circular Dependency Detection
        example6_CircularDependencyDetection();

        // Example 7: Performance with Reflection Cache
        example7_PerformanceWithCache();

        // Example 8: Using birth() alias
        example8_BirthAlias();
    }

    static void example1_BasicUsage() {
        System.out.println("--- Example 1: Basic Usage ---");
        
        User user = Mother.forClass(User.class).build();
        
        System.out.println("Generated user: " + user);
        System.out.println();
    }

    static void example2_ExcludeFields() {
        System.out.println("--- Example 2: Exclude Sensitive Fields ---");
        
        User user = Mother.forClass(User.class)
                .excludeFields("password", "internalId")
                .build();
        
        System.out.println("User without password and internalId: " + user);
        System.out.println("Password is null: " + (user.getPassword() == null));
        System.out.println("InternalId is null: " + (user.getInternalId() == null));
        System.out.println();
    }

    static void example3_CustomFieldGenerators() {
        System.out.println("--- Example 3: Custom Field Generators ---");
        
        User user = Mother.forClass(User.class)
                .forField("email", () -> "john.doe@example.com")
                .forField("age", () -> 30)
                .forField("active", () -> true)
                .build();
        
        System.out.println("User with custom fields: " + user);
        System.out.println("Email: " + user.getEmail());
        System.out.println("Age: " + user.getAge());
        System.out.println("Active: " + user.isActive());
        System.out.println();
    }

    static void example4_CombinedFeatures() {
        System.out.println("--- Example 4: Combined Features ---");
        
        User user = Mother.forClass(User.class)
                .forField("email", () -> "admin@example.com")
                .excludeFields("password", "internalId")
                .override("active", true)
                .withCollectionSize(2, 5)
                .build();
        
        System.out.println("User with combined features: " + user);
        System.out.println();
    }

    static void example5_TypeReference() {
        System.out.println("--- Example 5: TypeReference for Generic Types ---");
        
        // Create a TypeReference for List<String>
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        
        List<String> list = Mother.forType(typeRef).build();
        
        System.out.println("Generated list: " + list);
        System.out.println("List size: " + (list != null ? list.size() : 0));
        System.out.println();
    }

    static void example6_CircularDependencyDetection() {
        System.out.println("--- Example 6: Circular Dependency Detection ---");
        
        // This used to cause infinite loop, now it's handled gracefully
        Node node = Mother.forClass(Node.class).build();
        
        System.out.println("Generated node: " + node);
        System.out.println("Node has value: " + node.getValue());
        System.out.println("Node.next is null (cycle broken): " + (node.getNext() == null));
        System.out.println();
    }

    static void example7_PerformanceWithCache() {
        System.out.println("--- Example 7: Performance with Reflection Cache ---");
        
        // First generation (cache miss)
        long start1 = System.nanoTime();
        User user1 = Mother.forClass(User.class).build();
        long time1 = System.nanoTime() - start1;
        
        // Second generation (cache hit)
        long start2 = System.nanoTime();
        User user2 = Mother.forClass(User.class).build();
        long time2 = System.nanoTime() - start2;
        
        // Third generation (cache hit)
        long start3 = System.nanoTime();
        User user3 = Mother.forClass(User.class).build();
        long time3 = System.nanoTime() - start3;
        
        System.out.println("First generation time: " + (time1 / 1000) + " µs");
        System.out.println("Second generation time: " + (time2 / 1000) + " µs");
        System.out.println("Third generation time: " + (time3 / 1000) + " µs");
        System.out.println("Speedup: ~" + (time1 / Math.max(time2, 1)) + "x");
        System.out.println();
    }

    static void example8_BirthAlias() {
        System.out.println("--- Example 8: Using birth() Alias ---");
        
        // birth() is a semantic alias for build()
        User user = Mother.forClass(User.class)
                .override("email", "mother@example.com")
                .birth();  // More semantic than .build()
        
        System.out.println("User born from Mother: " + user);
        System.out.println();
    }
}

