/**
 * CryptographyUtils.java
 * <p>
 * Este arquivo contém utilitários para operações criptográficas, incluindo geração de IVs aleatórios,
 * encriptação e decriptação com AES em modo CBC, cálculo de hashes SHA-256 e manipulação de strings hexadecimais.
 * <p>
 * Autores:
 * - Cassio Silva (c.jones@edu.pucrs.br)
 * - Arthur Gil (a.gil@edu.pucrs.br)
 * <p>
 * Data de Criação: 20/11/2024
 * Última Modificação: 23/11/2024
 *
 */


package security;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

import static config.Constants.*;


public final class CryptographyUtils {

    private CryptographyUtils() {
        // Impede a instanciação.
    }

    /**
     * Gera um vetor de inicialização (IV) aleatório de 16 bytes.
     *
     * @return Um array de bytes representando o IV.
     */
    public static byte @NotNull [] generateRandomIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * Calcula o hash SHA-256 de um array de bytes.
     *
     * @param input Dados de entrada.
     * @return O hash SHA-256 como array de bytes.
     * @throws Exception Se ocorrer um erro ao inicializar o algoritmo de hash.
     */
    public static byte[] sha256(byte[] input) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("Entrada para SHA-256 não pode ser nula.");
        }
        return MessageDigest.getInstance(HASH_ALGORITHM).digest(input);
    }

    /**
     * Encripta dados usando o algoritmo AES em modo CBC com preenchimento PKCS5.
     *
     * @param data Dados a serem encriptados.
     * @param key  Chave secreta de 16 bytes.
     * @param iv   Vetor de inicialização (IV) de 16 bytes.
     * @return Dados encriptados como array de bytes.
     * @throws Exception Se ocorrer um erro durante o processo de encriptação.
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        validateKeyAndIv(key, iv);
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    /**
     * Decripta dados usando o algoritmo AES em modo CBC com preenchimento PKCS5.
     *
     * @param encryptedData Dados encriptados.
     * @param key           Chave secreta de 16 bytes.
     * @param iv            Vetor de inicialização (IV) de 16 bytes.
     * @return Dados decriptados como array de bytes.
     * @throws Exception Se ocorrer um erro durante o processo de decriptação.
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) throws Exception {
        validateKeyAndIv(key, iv);
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedData);
    }

    /**
     * Converte uma string hexadecimal em um array de bytes.
     *
     * @param hexString String hexadecimal.
     * @return Array de bytes correspondente.
     */
    public static byte @NotNull [] hexStringToByteArray(@NotNull String hexString) {
        if (hexString.isEmpty()) {
            throw new IllegalArgumentException("A string hexadecimal não pode ser nula ou vazia.");
        }
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString; // Ajusta comprimento ímpar.
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
     * Retorna uma instância de {@link Cipher} configurada para o modo especificado.
     *
     * @param mode Modo do cipher (ENCRYPT_MODE ou DECRYPT_MODE).
     * @param key  Chave secreta de 16 bytes.
     * @param iv   Vetor de inicialização (IV) de 16 bytes.
     * @return Uma instância de {@link Cipher}.
     * @throws Exception Se ocorrer um erro ao inicializar o cipher.
     */
    public static @NotNull Cipher getCipher(int mode, byte[] key, byte[] iv) throws Exception {
        validateKeyAndIv(key, iv);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(mode, new SecretKeySpec(key, AES_ALGORITHM), new IvParameterSpec(iv));
        return cipher;
    }

    /**
     * Valida o tamanho da chave e do IV para garantir compatibilidade com AES.
     *
     * @param key Chave secreta.
     * @param iv  Vetor de inicialização.
     */
    private static void validateKeyAndIv(byte[] key, byte[] iv) {
        if (key == null || key.length != 16) {
            throw new IllegalArgumentException("A chave deve ter exatamente 16 bytes.");
        }
        if (iv == null || iv.length != 16) {
            throw new IllegalArgumentException("O vetor de inicialização (IV) deve ter exatamente 16 bytes.");
        }
    }
}
