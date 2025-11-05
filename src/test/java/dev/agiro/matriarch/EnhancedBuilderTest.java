package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the enhanced builder pattern with new features.
 */
class EnhancedBuilderTest {

    static class User {
        private String email;
        private String password;
        private String internalId;
        private LocalDate birthDate;
        private int age;
        private boolean active;

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
    }

    @Test
    void testExcludeFields() {
        // Test excluding specific fields
        User user = Mother.forClass(User.class)
                .excludeFields("password", "internalId")
                .build();

        assertNotNull(user);
        assertNotNull(user.getEmail());
        assertNull(user.getPassword(), "Password should be excluded");
        assertNull(user.getInternalId(), "InternalId should be excluded");
    }

    @Test
    void testForFieldWithCustomGenerator() {
        // Test custom field generator
        User user = Mother.forClass(User.class)
                .forField("email", () -> "custom@example.com")
                .forField("age", () -> 25)
                .build();

        assertNotNull(user);
        assertEquals("custom@example.com", user.getEmail());
        assertEquals(25, user.getAge());
    }

    @Test
    void testCombinedBuilderFeatures() {
        // Test combining multiple builder features
        User user = Mother.forClass(User.class)
                .forField("email", () -> "test@example.com")
                .excludeFields("password", "internalId")
                .override("active", true)
                .build();

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getInternalId());
        assertTrue(user.isActive());
    }

    @Test
    void testWithCollectionSize() {
        // Test collection size configuration
        class Container {
            private List<String> items;
            
            public List<String> getItems() { return items; }
            public void setItems(List<String> items) { this.items = items; }
        }

        Container container = Mother.forClass(Container.class)
                .withCollectionSize(5, 10)
                .build();

        assertNotNull(container);
        // Note: Collection size will need to be integrated with the generator
    }

    @Test
    void testBirthAlias() {
        // Test that birth() is an alias for build()
        User user1 = Mother.forClass(User.class).build();
        User user2 = Mother.forClass(User.class).birth();

        assertNotNull(user1);
        assertNotNull(user2);
    }
}

