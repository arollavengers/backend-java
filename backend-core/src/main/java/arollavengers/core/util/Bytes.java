package arollavengers.core.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Bytes {

    public static byte[] toBytes(char[] chars) {
        byte[] bytes = new byte[chars.length << 1];
        writeBytes(chars, 0, bytes);
        return bytes;
    }

    private static void writeBytes(char[] chars, int offset, byte[] dst) {
        for (int i = 0; i < chars.length; i++) {
            int bpos = (i + offset) << 1;
            dst[bpos] = (byte) ((chars[i] & 0xFF00) >> 8);
            dst[bpos + 1] = (byte) ((chars[i]));
        }
    }
}
