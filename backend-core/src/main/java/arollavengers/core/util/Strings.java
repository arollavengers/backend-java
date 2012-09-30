package arollavengers.core.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Strings {
    /**
     * Converts a camelCase string to a separated one. Receives the separator character
     *
     * @param text      the text to transform
     * @param separator the separator character to use
     * @return the separated text.
     */
    public static String separate(String text, char separator) {
        StringBuilder builder = new StringBuilder(text.length() + text.length() / 2);
        int l = text.length();
        for (int i = 0; i < l; i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    builder.append(separator);
                }
                builder.append(Character.toLowerCase(c));
            }
            else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
