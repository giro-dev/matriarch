package dev.agiro.matriarch;

import dev.agiro.matriarch.util.RegexGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexGeneratorTest {

    @Test
    @DisplayName("Should generate string matching digit regex")
    void testDigitRegex() {
        String result = RegexGenerator.generate("\\d{4}");
        assertNotNull(result);
        assertEquals(4, result.length());
        assertTrue(result.matches("\\d{4}"), "Generated: " + result);
    }

    @Test
    @DisplayName("Should generate string matching complex email regex")
    void testEmailRegex() {
        String result = RegexGenerator.generate("[a-z]{4,8}\\.[a-z]{4,8}_[a-z]{4,8}\\@(gmail|outlook).com");
        assertNotNull(result);
        assertTrue(result.matches("[a-z]{4,8}\\.[a-z]{4,8}_[a-z]{4,8}@(gmail|outlook)\\.com"), 
                   "Generated: " + result);
    }

    @Test
    @DisplayName("Should generate string matching digit range regex")
    void testDigitRangeRegex() {
        String result = RegexGenerator.generate("1[0-9]{3}");
        assertNotNull(result);
        assertEquals(4, result.length());
        assertTrue(result.matches("1[0-9]{3}"), "Generated: " + result);
        int value = Integer.parseInt(result);
        assertTrue(value >= 1000 && value < 2000, "Generated value: " + value);
    }

    @Test
    @DisplayName("Should generate string with character class")
    void testCharacterClass() {
        String result = RegexGenerator.generate("[abc]");
        assertNotNull(result);
        assertEquals(1, result.length());
        assertTrue("abc".contains(result), "Generated: " + result);
    }

    @Test
    @DisplayName("Should generate string with range")
    void testRange() {
        String result = RegexGenerator.generate("[0-9]");
        assertNotNull(result);
        assertEquals(1, result.length());
        assertTrue(result.matches("[0-9]"), "Generated: " + result);
    }

    @Test
    @DisplayName("Should handle quantifiers")
    void testQuantifiers() {
        // Test {n}
        String result1 = RegexGenerator.generate("a{3}");
        assertEquals("aaa", result1);
        
        // Test {n,m}
        String result2 = RegexGenerator.generate("b{2,4}");
        assertTrue(result2.length() >= 2 && result2.length() <= 4);
        assertTrue(result2.matches("b{2,4}"));
    }
}

