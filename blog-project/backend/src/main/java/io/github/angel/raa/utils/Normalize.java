package io.github.angel.raa.utils;

import java.util.regex.Pattern;

/**
 * Class  to normalize strings for use in URLs or file names.
 * <p>
 * This class is used to normalize strings for use in URLs or file names.
 * It removes non-latin characters, whitespace, and dashes from the input string.
 * It also removes leading and trailing dashes.
 * </p>
 * Example:
 * <pre>{@code
 * String input = "Hello World!";
 * String slug = Normalize.slugify(input);
 * System.out.println(slug);
 * out: hello-world
 *
 * }
 * </pre>
 */
public class Normalize {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDASHES = Pattern.compile("(^-|-$)");

    /**
     * Normalizes a string for use in URLs or file names.
     *
     * @param input The string to normalize.
     */
    public static String slugify(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = java.text.Normalizer.normalize(nowhitespace, java.text.Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGESDASHES.matcher(slug).replaceAll("");
        return slug.toLowerCase();
    }

}
