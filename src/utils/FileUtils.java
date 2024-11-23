package utils;

import org.jetbrains.annotations.NotNull;
import security.CryptographyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Classe utilitária para operações de arquivo.
 */
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
