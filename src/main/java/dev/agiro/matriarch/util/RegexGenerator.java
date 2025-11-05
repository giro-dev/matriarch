package dev.agiro.matriarch.util;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
/**
 * Simple regex-to-string generator that supports common regex patterns.
 * This is a lightweight alternative to datafaker's regexify method.
 */
public class RegexGenerator {
    private static final SecureRandom random = new SecureRandom();
    /**
     * Generate a random string that matches the given regex pattern.
     * Supports:
     * - Character classes: [a-z], [A-Z], [0-9], [abc]
     * - Quantifiers: {n}, {n,m}, *, +, ?
     * - Groups: () with alternation |
     * - Escaped characters: \\d, \\w, \\s
     * - Literals
     */
    public static String generate(String regex) {
        if (regex == null || regex.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < regex.length()) {
            char c = regex.charAt(i);
            if (c == '\\' && i + 1 < regex.length()) {
                // Handle escaped characters
                char next = regex.charAt(i + 1);
                String generated = handleEscapedChar(next);
                QuantifierInfo quantifier = extractQuantifier(regex, i + 2);
                appendWithQuantifier(result, generated, quantifier);
                i = quantifier.endIndex;
            } else if (c == '[') {
                // Handle character class
                int end = findMatchingBracket(regex, i);
                String charClass = regex.substring(i + 1, end);
                String generated = generateFromCharClass(charClass);
                QuantifierInfo quantifier = extractQuantifier(regex, end + 1);
                appendWithQuantifier(result, generated, quantifier);
                i = quantifier.endIndex;
            } else if (c == '(') {
                // Handle group
                int end = findMatchingParenthesis(regex, i);
                String group = regex.substring(i + 1, end);
                // Check if the group contains alternation
                String generated;
                if (group.contains("|")) {
                    // Handle alternation within the group - pick ONE alternative
                    String[] alternatives = splitAlternation(group);
                    generated = generate(alternatives[random.nextInt(alternatives.length)]);
                } else {
                    generated = generate(group);
                }
                QuantifierInfo quantifier = extractQuantifier(regex, end + 1);
                appendWithQuantifier(result, generated, quantifier);
                i = quantifier.endIndex;
            } else if (c == '|') {
                // Handle top-level alternation (shouldn't normally reach here if properly in groups)
                // We've already processed everything before the pipe
                // Now randomly choose from what comes after
                String[] parts = splitAlternation(regex.substring(i + 1));
                result.append(generate(parts[random.nextInt(parts.length)]));
                break;
            } else if (isQuantifier(c)) {
                // Quantifier without preceding element - skip
                i++;
            } else {
                // Literal character
                QuantifierInfo quantifier = extractQuantifier(regex, i + 1);
                appendWithQuantifier(result, String.valueOf(c), quantifier);
                i = quantifier.endIndex;
            }
        }
        return result.toString();
    }
    private static String handleEscapedChar(char c) {
        return switch (c) {
            case 'd' -> String.valueOf(random.nextInt(10));
            case 'w' -> generateFromCharClass("a-zA-Z0-9_");
            case 'W' -> generateFromCharClass("!@#$%^&*()");
            case 's' -> " ";
            case 'S' -> "a";
            case 't' -> "\t";
            case 'n' -> "\n";
            case 'r' -> "\r";
            default -> String.valueOf(c); // Literal escaped character
        };
    }
    private static String generateFromCharClass(String charClass) {
        List<Character> chars = new ArrayList<>();
        int i = 0;
        while (i < charClass.length()) {
            if (i + 2 < charClass.length() && charClass.charAt(i + 1) == '-') {
                // Range like a-z
                char start = charClass.charAt(i);
                char end = charClass.charAt(i + 2);
                for (char c = start; c <= end; c++) {
                    chars.add(c);
                }
                i += 3;
            } else {
                // Single character
                chars.add(charClass.charAt(i));
                i++;
            }
        }
        if (chars.isEmpty()) {
            return "";
        }
        return String.valueOf(chars.get(random.nextInt(chars.size())));
    }
    private static int findMatchingBracket(String regex, int start) {
        int depth = 1;
        for (int i = start + 1; i < regex.length(); i++) {
            if (regex.charAt(i) == '[') {
                depth++;
            } else if (regex.charAt(i) == ']') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return regex.length();
    }
    private static int findMatchingParenthesis(String regex, int start) {
        int depth = 1;
        for (int i = start + 1; i < regex.length(); i++) {
            if (regex.charAt(i) == '\\' && i + 1 < regex.length()) {
                i++; // Skip escaped character
                continue;
            }
            if (regex.charAt(i) == '(') {
                depth++;
            } else if (regex.charAt(i) == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return regex.length();
    }
    private static QuantifierInfo extractQuantifier(String regex, int start) {
        if (start >= regex.length()) {
            return new QuantifierInfo(1, 1, start);
        }
        char c = regex.charAt(start);
        if (c == '*') {
            int count = random.nextInt(5); // 0 to 4
            return new QuantifierInfo(count, count, start + 1);
        } else if (c == '+') {
            int count = random.nextInt(5) + 1; // 1 to 5
            return new QuantifierInfo(count, count, start + 1);
        } else if (c == '?') {
            int count = random.nextInt(2); // 0 or 1
            return new QuantifierInfo(count, count, start + 1);
        } else if (c == '{') {
            int end = regex.indexOf('}', start);
            if (end == -1) {
                return new QuantifierInfo(1, 1, start);
            }
            String quantifier = regex.substring(start + 1, end);
            if (quantifier.contains(",")) {
                String[] parts = quantifier.split(",");
                int min = Integer.parseInt(parts[0].trim());
                int max = parts.length > 1 && !parts[1].trim().isEmpty() 
                    ? Integer.parseInt(parts[1].trim()) 
                    : min + 5;
                int count = min + random.nextInt(max - min + 1);
                return new QuantifierInfo(count, count, end + 1);
            } else {
                int count = Integer.parseInt(quantifier.trim());
                return new QuantifierInfo(count, count, end + 1);
            }
        }
        return new QuantifierInfo(1, 1, start);
    }
    private static void appendWithQuantifier(StringBuilder result, String s, QuantifierInfo quantifier) {
        result.append(s.repeat(quantifier.min));
    }
    private static boolean isQuantifier(char c) {
        return c == '*' || c == '+' || c == '?' || c == '{';
    }
    private static String[] splitAlternation(String s) {
        // Simple split on pipe - doesn't handle nested groups, but sufficient for our use case
        return s.split("\\|");
    }
    private static class QuantifierInfo {
        final int min;
        final int max;
        final int endIndex;
        QuantifierInfo(int min, int max, int endIndex) {
            this.min = min;
            this.max = max;
            this.endIndex = endIndex;
        }
    }
}
