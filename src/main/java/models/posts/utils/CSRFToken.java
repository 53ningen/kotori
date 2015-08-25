package models.posts.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public class CSRFToken {
    private static final String ALGORITHM = "NativePRNG";
    private static final int TOKEN_LENGTH = 16;

    /**
     * CSRFトークンを返す
     * @return トークン
     */
    public static String getCSRFToken() {
        byte[] token = new byte[TOKEN_LENGTH];
        StringBuilder sb = new StringBuilder();
        SecureRandom random;

        try {
            random = SecureRandom.getInstance(ALGORITHM);
            random.nextBytes(token);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }

        IntStream.range(0, token.length)
                .map(i -> token[i])
                .forEach(i -> sb.append(String.format("%02x", i & 0xff)));

        return sb.toString();
    }

}
