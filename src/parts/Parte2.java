/**
 * Parte2.java
 * <p>
 * This file implements secure message processing, including decryption,
 * reversing the content, and re-encrypting using AES encryption in CBC mode.
 * <p>
 * Authors:
 * - Cassio Silva (c.jones@edu.pucrs.br)
 * - Arthur Gil (a.gil@edu.pucrs.br)
 * <p>
 * Creation Date: 21/11/2024
 * Last Modified: 23/11/2024
 *
 */

package parts;

import security.CryptographyUtils;
import utils.FileUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public final class Parte2 {

    /**
     * Processes the encrypted message: decrypts, reverses, and saves the results.
     *
     * @throws Exception If an error occurs during processing.
     */
    public static void processMessage() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Read the secret key stored in 'S.key'
        byte[] secretKey = FileUtils.readStringAsBytes("S.key");

        // Check if the encrypted message exists
        Path messagePath = Path.of("message.enc");
        byte[] messageBytes = Files.exists(messagePath)
                ? getMessageBytes(scanner, messagePath)
                : requestNewMessage(scanner, messagePath);

        // Separate IV and ciphertext
        byte[] iv = Arrays.copyOfRange(messageBytes, 0, 16);
        byte[] ciphertext = Arrays.copyOfRange(messageBytes, 16, messageBytes.length);

        // Decrypt and reverse the message in parallel
        CompletableFuture<String> decryptedFuture = CompletableFuture.supplyAsync(() -> decryptMessage(ciphertext, secretKey, iv));
        CompletableFuture<String> invertedFuture = decryptedFuture.thenApplyAsync(Parte2::invertMessage);

        // Encrypt the reversed message
        String invertedPlaintext = invertedFuture.get();
        byte[] newIv = CryptographyUtils.generateRandomIv();
        byte[] encryptedInvertedBytes = CryptographyUtils.encrypt(invertedPlaintext.getBytes(StandardCharsets.UTF_8), secretKey, newIv);

        // Concatenate IV and ciphertext
        byte[] newMessage = new byte[newIv.length + encryptedInvertedBytes.length];
        System.arraycopy(newIv, 0, newMessage, 0, newIv.length);
        System.arraycopy(encryptedInvertedBytes, 0, newMessage, newIv.length, encryptedInvertedBytes.length);

        // Convert to hexadecimal and save to 'message_inverted.enc'
        FileUtils.writeBytesAsHex("message_inverted.enc", newMessage);

        System.out.println("Reversed encrypted message saved in 'message_inverted.enc'.");
        System.out.println("Send the file 'message_inverted.enc' to the professor.");
    }

    /**
     * Gets the bytes of an existing message or prompts the user for new input.
     *
     * @param scanner     Scanner for user input.
     * @param messagePath Path to the message file.
     * @return Encrypted message bytes.
     * @throws Exception If an error occurs during reading.
     */
    private static byte[] getMessageBytes( Scanner scanner, Path messagePath) throws Exception {
        System.out.println("The file 'message.enc' exists. Do you want to use the existing message? (Y/N)");
        String choice = scanner.nextLine().trim().toUpperCase();
        if (choice.equals("Y")) {
            return FileUtils.readStringAsBytes(messagePath.toString());
        } else {
            return requestNewMessage(scanner, messagePath);
        }
    }

    /**
     * Prompts the user for a new encrypted message and saves it to a file.
     *
     * @param scanner     Scanner for user input.
     * @param messagePath Path to the file where the message will be saved.
     * @return Encrypted message bytes.
     * @throws Exception If an error occurs during writing.
     */
    private static byte  [] requestNewMessage( Scanner scanner,  Path messagePath) throws Exception {
        System.out.println("Enter the encrypted message (in hexadecimal):");
        String messageHex = scanner.nextLine().trim();
        byte[] messageBytes = CryptographyUtils.hexStringToByteArray(messageHex);
        FileUtils.writeString(messagePath.toString(), messageHex);
        return messageBytes;
    }

    /**
     * Decrypts the message using the key and IV.
     *
     * @param ciphertext Encrypted text.
     * @param secretKey  Secret key.
     * @param iv         Initialization vector (IV).
     * @return Decrypted message as plain text.
     */
    private static  String decryptMessage(byte[] ciphertext, byte[] secretKey, byte[] iv) {
        try {
            Cipher cipher = CryptographyUtils.getCipher(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] plaintextBytes = cipher.doFinal(ciphertext);
            String plaintext = new String(plaintextBytes, StandardCharsets.UTF_8);
            System.out.println("Decrypted message: " + plaintext);
            return plaintext;
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting the message.", e);
        }
    }

    /**
     * Reverses the given message.
     *
     * @param plaintext Original message.
     * @return Reversed message.
     */
    private static  String invertMessage(String plaintext) {
        String inverted = new StringBuilder(plaintext).reverse().toString();
        System.out.println("Reversed message: " + inverted);
        return inverted;
    }
}