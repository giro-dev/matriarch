package dev.agiro.matriarch.domain.core;

/**
 * Simple record for regex patterns.
 * Extracted from Mother class to follow proper separation of concerns.
 * This is a value object that can be used across the domain.
 */
public record Regex(String value) {
}

