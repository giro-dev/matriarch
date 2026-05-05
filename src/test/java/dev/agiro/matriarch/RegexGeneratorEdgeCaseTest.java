package dev.agiro.matriarch;

import dev.agiro.matriarch.util.RegexGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge-case coverage for {@link RegexGenerator}.
 * The regex grammar implemented is documented in the class itself
 * (character classes, quantifiers, groups with alternation, escapes,
 * literals). These tests pin that grammar so accidental regressions
 * are caught.
 */
class RegexGeneratorEdgeCaseTest {

    @Test
    @DisplayName("null and empty patterns should produce empty strings")
    void nullAndEmptyInputs() {
        assertEquals("", RegexGenerator.generate(null));
        assertEquals("", RegexGenerator.generate(""));
    }

    @Test
    @DisplayName("plain literal without quantifier echoes itself")
    void plainLiteral() {
        assertEquals("hello", RegexGenerator.generate("hello"));
    }

    @Test
    @DisplayName("escaped metacharacters appear as literals")
    void escapedMetacharacters() {
        assertEquals(".", RegexGenerator.generate("\\."));
        assertEquals("(", RegexGenerator.generate("\\("));
        assertEquals(")", RegexGenerator.generate("\\)"));
        assertEquals("[", RegexGenerator.generate("\\["));
        assertEquals("]", RegexGenerator.generate("\\]"));
        assertEquals("\\", RegexGenerator.generate("\\\\"));
    }

    @Test
    @DisplayName("\\d, \\t, \\n, \\r, escape codes")
    void escapeCodes() {
        for (int i = 0; i < 50; i++) {
            String s = RegexGenerator.generate("\\d");
            assertEquals(1, s.length());
            assertTrue(Character.isDigit(s.charAt(0)));
        }
        assertEquals("\t", RegexGenerator.generate("\\t"));
        assertEquals("\n", RegexGenerator.generate("\\n"));
        assertEquals("\r", RegexGenerator.generate("\\r"));
    }

    @RepeatedTest(20)
    @DisplayName("character class ranges produce a single char in range")
    void characterClassRanges() {
        String s = RegexGenerator.generate("[a-z]");
        assertEquals(1, s.length());
        assertTrue(s.charAt(0) >= 'a' && s.charAt(0) <= 'z', "got: " + s);

        s = RegexGenerator.generate("[A-Z]");
        assertEquals(1, s.length());
        assertTrue(s.charAt(0) >= 'A' && s.charAt(0) <= 'Z', "got: " + s);

        s = RegexGenerator.generate("[0-9]");
        assertEquals(1, s.length());
        assertTrue(s.charAt(0) >= '0' && s.charAt(0) <= '9', "got: " + s);
    }

    @Test
    @DisplayName("character class with literal chars only")
    void characterClassLiterals() {
        for (int i = 0; i < 50; i++) {
            String s = RegexGenerator.generate("[xyz]");
            assertEquals(1, s.length());
            assertTrue("xyz".contains(s), "got: " + s);
        }
    }

    @Test
    @DisplayName("character class combining range and literals")
    void characterClassRangeAndLiteral() {
        for (int i = 0; i < 50; i++) {
            String s = RegexGenerator.generate("[a-c0-9_]");
            assertEquals(1, s.length());
            assertTrue(s.matches("[a-c0-9_]"), "got: " + s);
        }
    }

    @RepeatedTest(20)
    @DisplayName("{n} produces exactly n repetitions")
    void exactQuantifier() {
        assertEquals(7, RegexGenerator.generate("[a-z]{7}").length());
        assertEquals("aaaaa", RegexGenerator.generate("a{5}"));
    }

    @RepeatedTest(40)
    @DisplayName("{n,m} produces between n and m repetitions")
    void rangeQuantifier() {
        String s = RegexGenerator.generate("[a-z]{2,4}");
        assertTrue(s.length() >= 2 && s.length() <= 4, "len=" + s.length() + " s=" + s);
        assertTrue(s.matches("[a-z]{2,4}"), "got: " + s);
    }

    @RepeatedTest(20)
    @DisplayName("? produces 0 or 1 character")
    void optionalQuantifier() {
        String s = RegexGenerator.generate("a?");
        assertTrue("".equals(s) || "a".equals(s), "got: " + s);
    }

    @RepeatedTest(20)
    @DisplayName("+ produces at least one repetition")
    void plusQuantifier() {
        String s = RegexGenerator.generate("a+");
        assertFalse(s.isEmpty(), "+ must produce at least one");
        assertTrue(s.matches("a+"), "got: " + s);
    }

    @RepeatedTest(20)
    @DisplayName("alternation in groups picks one branch")
    void groupAlternation() {
        String s = RegexGenerator.generate("(cat|dog|fish)");
        assertTrue("cat".equals(s) || "dog".equals(s) || "fish".equals(s), "got: " + s);
    }

    @RepeatedTest(20)
    @DisplayName("group with quantifier")
    void groupWithQuantifier() {
        String s = RegexGenerator.generate("(ab){3}");
        assertEquals("ababab", s);
    }

    @RepeatedTest(20)
    @DisplayName("nested groups")
    void nestedGroups() {
        String s = RegexGenerator.generate("((foo)|(bar))");
        assertTrue("foo".equals(s) || "bar".equals(s), "got: " + s);
    }

    @RepeatedTest(20)
    @DisplayName("composite phone-like pattern")
    void phonePattern() {
        String s = RegexGenerator.generate("\\d{3}-\\d{3}-\\d{4}");
        assertTrue(s.matches("\\d{3}-\\d{3}-\\d{4}"), "got: " + s);
    }

    @RepeatedTest(20)
    @DisplayName("composite email-like pattern")
    void emailPattern() {
        String s = RegexGenerator.generate("[a-z]{3,8}@(gmail|outlook)\\.com");
        assertTrue(s.matches("[a-z]{3,8}@(gmail|outlook)\\.com"), "got: " + s);
    }

    @Test
    @DisplayName("standalone quantifier is skipped without crashing")
    void leadingQuantifier() {
        // The implementation skips bare quantifiers — verifying it doesn't
        // throw and produces a reasonable string.
        String s = RegexGenerator.generate("*abc");
        assertNotNull(s);
        // The literal "abc" should still appear.
        assertTrue(s.contains("abc"), "got: " + s);
    }

    @Test
    @DisplayName("unmatched closing bracket is treated as literal")
    void unmatchedBracketRobustness() {
        // Should not throw — confirm the generator is robust to malformed
        // patterns rather than blowing up at runtime.
        assertDoesNotThrow(() -> RegexGenerator.generate("abc]"));
        assertDoesNotThrow(() -> RegexGenerator.generate("abc)"));
    }
}
