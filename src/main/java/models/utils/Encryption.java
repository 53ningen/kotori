package models.utils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.IntStream;

public class Encryption {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 2048;
    private static final int KEY_LENGTH = 256;

    /**
     * 平文の削除キーとソルトからハッシュ化されたキーを生成する
     * @param deleteKey 削除キー
     * @param salt ソルト（ユーザ名）
     * @return ハッシュ化されたキー
     */
    public static String getSaltedKey(String deleteKey, String salt) {
        SecretKey secretKey;

        try {
            char[] keyCharArray = deleteKey.toCharArray();
            byte[] hashedSalt = getHashedSalt(salt);
            PBEKeySpec keySpec = new PBEKeySpec(keyCharArray, hashedSalt, ITERATION_COUNT, KEY_LENGTH);
            secretKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return "";
        }

        byte[] passByteArray = secretKey.getEncoded();

        StringBuilder sb = new StringBuilder(64);
        IntStream
                .range(0, passByteArray.length)
                .map(i -> passByteArray[i])
                .forEach(i -> sb.append(String.format("%02x", i & 0xff)));

        return sb.toString();
    }

    /**
     * ソルトをハッシュ化する
     * @param salt ソルト
     * @return ハッシュ化されたバイト配列
     * @throws NoSuchAlgorithmException
     */
    private static byte[] getHashedSalt(String salt) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        md.update(salt.getBytes());
        return md.digest();
    }
}
