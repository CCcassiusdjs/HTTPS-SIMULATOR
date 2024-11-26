/**
 * CryptographyUtils.java
 * <p>
 * This file contains utilities for cryptographic operations, including generating random IVs,
 * AES encryption and decryption in CBC mode, SHA-256 hash computation, and hexadecimal string manipulation.
 * <p>
 * Authors:
 * - Cassio Silva (c.jones@edu.pucrs.br)
 * - Arthur Gil (a.gil@edu.pucrs.br)
 * <p>
 * Creation Date: 20/11/2024
 * Last Modified: 23/11/2024
 *
 */

package security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

import static config.Constants.*;

public final class CryptographyUtils {

    private CryptographyUtils() {
        // Prevent instantiation.
    }

    /**
     * Generates a random 16-byte initialization vector (IV).
     *
     * @return A byte array representing the IV.
     */
    public static byte  [] generateRandomIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * Computes the SHA-256 hash of a byte array.
     *
     * @param input Input data.
     * @return The SHA-256 hash as a byte array.
     * @throws Exception If an error occurs while initializing the hash algorithm.
     */
    public static byte[] sha256(byte[] input) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("Input for SHA-256 cannot be null.");
        }
        return MessageDigest.getInstance(HASH_ALGORITHM).digest(input);
    }

    /**
     * Encrypts data using the AES algorithm in CBC mode with PKCS5 padding.
     *
     * @param data Data to be encrypted.
     * @param key  Secret key of 16 bytes.
     * @param iv   16-byte initialization vector (IV).
     * @return Encrypted data as a byte array.
     * @throws Exception If an error occurs during the encryption process.
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        validateKeyAndIv(key, iv);
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    /**
     * Decrypts data using the AES algorithm in CBC mode with PKCS5 padding.
     *
     * @param encryptedData Encrypted data.
     * @param key           Secret key of 16 bytes.
     * @param iv            16-byte initialization vector (IV).
     * @return Decrypted data as a byte array.
     * @throws Exception If an error occurs during the decryption process.
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) throws Exception {
        validateKeyAndIv(key, iv);
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedData);
    }

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param hexString Hexadecimal string.
     * @return Corresponding byte array.
     */
    public static byte  [] hexStringToByteArray( String hexString) {
        if (hexString.isEmpty()) {
            throw new IllegalArgumentException("Hex string cannot be null or empty.");
        }
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString; // Adjust odd-length string.
            len++;
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Returns a {@link Cipher} instance configured for the specified mode.
     *
     * @param mode Cipher mode (ENCRYPT_MODE or DECRYPT_MODE).
     * @param key  Secret key of 16 bytes.
     * @param iv   16-byte initialization vector (IV).
     * @return A {@link Cipher} instance.
     * @throws Exception If an error occurs while initializing the cipher.
     */
    public static  Cipher getCipher(int mode, byte[] key, byte[] iv) throws Exception {
        validateKeyAndIv(key, iv);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(mode, new SecretKeySpec(key, AES_ALGORITHM), new IvParameterSpec(iv));
        return cipher;
    }

    /**
     * Validates the size of the key and IV to ensure AES compatibility.
     *
     * @param key Secret key.
     * @param iv  Initialization vector.
     */
    private static void validateKeyAndIv(byte[] key, byte[] iv) {
        if (key == null || key.length != 16) {
            throw new IllegalArgumentException("The key must be exactly 16 bytes.");
        }
        if (iv == null || iv.length != 16) {
            throw new IllegalArgumentException("The initialization vector (IV) must be exactly 16 bytes.");
        }
    }
}