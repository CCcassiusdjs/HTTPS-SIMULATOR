/**
 * FileUtils.java
 * <p>
 * Este arquivo contém utilitários para operações com arquivos, incluindo leitura e escrita de dados,
 * manipulação de strings e conversão entre bytes e hexadecimal, para suporte às funcionalidades do projeto.
 * <p>
 * Autores:
 * - Cassio Silva (c.jones@edu.pucrs.br)
 * - Arthur Gil (a.gil@edu.pucrs.br)
 * <p>
 * Data de Criação: 21/11/2024
 * Última Modificação: 23/11/2024
 *
 */


package utils;

import org.jetbrains.annotations.NotNull;
import security.CryptographyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FileUtils {
    private FileUtils() {
        // Impede a instanciação.
    }

    public static void writeToFile(String filename, List<String> lines) throws IOException {
        Files.write(Path.of(filename), lines);
    }

    public static @NotNull List<String> readFromFile(String filename) throws IOException {
        return Files.readAllLines(Path.of(filename));
    }

    public static String readFirstLine(String filename) throws IOException {
        try (var lines = Files.lines(Path.of(filename))) {
            return lines.findFirst().orElse("");
        }
    }


    public static void writeString(String filename, @NotNull String content) throws IOException {
        Files.writeString(Path.of(filename), content.strip());
    }

    public static byte @NotNull [] readStringAsBytes(String filename) throws IOException {
        String content = Files.readString(Path.of(filename)).trim();
        return CryptographyUtils.hexStringToByteArray(content);
    }

    public static void writeBytesAsHex(String filename, byte[] data) throws IOException {
        String hex = bytesToHex(data);
        writeString(filename, hex);
    }

    private static @NotNull String bytesToHex(byte @NotNull [] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
