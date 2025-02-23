package io.github.angel.raa.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NormalizeTest {

    @DisplayName("Test Slug")
    @Test
    void slugify() {
        String input = "Hello World!";
        String slug = Normalize.slugify(input);
        assertEquals("hello-world", slug);
    }

    @DisplayName("Test Slug with non-latin characters")
    @Test
    void slugifyWithNonLatinCharacters() {
        String input = "¡Hola Mundo!";
        String slug = Normalize.slugify(input);
        assertEquals("hola-mundo", slug);
    }

    @DisplayName("Test Slug with whitespace")
    @Test
    void slugifyWithWhitespace() {
        String input = "Hello World!";
        String slug = Normalize.slugify(input);
        assertEquals("hello-world", slug);
    }


}