package parts;

import org.jetbrains.annotations.NotNull;
import security.CryptographyUtils;
import utils.FileUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * Classe responsável pelo processamento da mensagem cifrada, decifração, inversão,
 * e salvamento da mensagem processada.
 */
public final class Parte2 {

    /**
     * Processa a mensagem cifrada, decifra, inverte e salva os resultados.
     *
     * @throws Exception Se ocorrer um erro durante o processamento.
     */
    public static void processMessage() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Lê a chave secreta armazenada em 'S.key'
        byte[] secretKey = FileUtils.readStringAsBytes("S.key");

        // Verificar se a mensagem cifrada existe
        Path messagePath = Path.of("message.enc");
        byte[] messageBytes = Files.exists(messagePath)
                ? getMessageBytes(scanner, messagePath)
                : requestNewMessage(scanner, messagePath);

        // Separar IV e texto cifrado
        byte[] iv = Arrays.copyOfRange(messageBytes, 0, 16);
        byte[] ciphertext = Arrays.copyOfRange(messageBytes, 16, messageBytes.length);

        // Decifrar e inverter a mensagem em paralelo
        CompletableFuture<String> decryptedFuture = CompletableFuture.supplyAsync(() -> decryptMessage(ciphertext, secretKey, iv));
        CompletableFuture<String> invertedFuture = decryptedFuture.thenApplyAsync(Parte2::invertMessage);

        // Cifrar a mensagem invertida
        String invertedPlaintext = invertedFuture.get();
        byte[] newIv = CryptographyUtils.generateRandomIv();
        byte[] encryptedInvertedBytes = CryptographyUtils.encrypt(invertedPlaintext.getBytes(StandardCharsets.UTF_8), secretKey, newIv);

        // Concatenar IV e texto cifrado
        byte[] newMessage = new byte[newIv.length + encryptedInvertedBytes.length];
        System.arraycopy(newIv, 0, newMessage, 0, newIv.length);
        System.arraycopy(encryptedInvertedBytes, 0, newMessage, newIv.length, encryptedInvertedBytes.length);

        // Converter para hexadecimal e salvar no arquivo 'message_inverted.enc'
        FileUtils.writeBytesAsHex("message_inverted.enc", newMessage);

        System.out.println("Mensagem invertida cifrada salva em 'message_inverted.enc'.");
        System.out.println("Envie o arquivo 'message_inverted.enc' ao professor.");
    }

    /**
     * Obtém os bytes da mensagem existente ou solicita uma nova entrada ao usuário.
     *
     * @param scanner     Scanner para entrada do usuário.
     * @param messagePath Caminho do arquivo de mensagem.
     * @return Bytes da mensagem cifrada.
     * @throws Exception Se ocorrer um erro durante a leitura.
     */
    private static byte[] getMessageBytes(@NotNull Scanner scanner, Path messagePath) throws Exception {
        System.out.println("O arquivo 'message.enc' existe. Deseja usar a mensagem existente? (S/N)");
        String choice = scanner.nextLine().trim().toUpperCase();
        if (choice.equals("S")) {
            return FileUtils.readStringAsBytes(messagePath.toString());
        } else {
            return requestNewMessage(scanner, messagePath);
        }
    }

    /**
     * Solicita uma nova mensagem cifrada ao usuário e a salva em um arquivo.
     *
     * @param scanner     Scanner para entrada do usuário.
     * @param messagePath Caminho do arquivo onde a mensagem será salva.
     * @return Bytes da mensagem cifrada.
     * @throws Exception Se ocorrer um erro durante a escrita.
     */
    private static byte @NotNull [] requestNewMessage(@NotNull Scanner scanner, @NotNull Path messagePath) throws Exception {
        System.out.println("Digite a mensagem cifrada (em hexadecimal):");
        String messageHex = scanner.nextLine().trim();
        byte[] messageBytes = CryptographyUtils.hexStringToByteArray(messageHex);
        FileUtils.writeString(messagePath.toString(), messageHex);
        return messageBytes;
    }

    /**
     * Decifra a mensagem utilizando a chave e o IV.
     *
     * @param ciphertext Texto cifrado.
     * @param secretKey  Chave secreta.
     * @param iv         Vetor de inicialização (IV).
     * @return Mensagem decifrada como texto plano.
     */
    private static @NotNull String decryptMessage(byte[] ciphertext, byte[] secretKey, byte[] iv) {
        try {
            Cipher cipher = CryptographyUtils.getCipher(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] plaintextBytes = cipher.doFinal(ciphertext);
            String plaintext = new String(plaintextBytes, StandardCharsets.UTF_8);
            System.out.println("Mensagem decifrada: " + plaintext);
            return plaintext;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decifrar a mensagem.", e);
        }
    }

    /**
     * Inverte a mensagem fornecida.
     *
     * @param plaintext Mensagem original.
     * @return Mensagem invertida.
     */
    private static @NotNull String invertMessage(String plaintext) {
        String inverted = new StringBuilder(plaintext).reverse().toString();
        System.out.println("Mensagem invertida: " + inverted);
        return inverted;
    }
}
