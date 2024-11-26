/**
 * FileUtils.java
 * <p>
 * This file contains utilities for file operations, including reading and writing data,
 * string manipulation, and conversion between bytes and hexadecimal, to support the project's functionalities.
 * <p>
 * Authors:
 * - Cassio Silva (c.jones@edu.pucrs.br)
 * - Arthur Gil (a.gil@edu.pucrs.br)
 * <p>
 * Creation Date: 21/11/2024
 * Last Modified: 23/11/2024
 *
 */

package utils;

import security.CryptographyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FileUtils {
    private FileUtils() {
        // Prevent instantiation.
    }

    /**
     * Writes a list of strings to a file.
     *
     * @param filename The name of the file to write to.
     * @param lines    The list of strings to write.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeToFile(String filename, List<String> lines) throws IOException {
        Files.write(Path.of(filename), lines);
    }

    /**
     * Reads all lines from a file into a list of strings.
     *
     * @param filename The name of the file to read from.
     * @return A list of strings representing the file content.
     * @throws IOException If an I/O error occurs.
     */
    public static  List<String> readFromFile(String filename) throws IOException {
        return Files.readAllLines(Path.of(filename));
    }

    /**
     * Reads the first line of a file.
     *
     * @param filename The name of the file to read from.
     * @return The first line of the file as a string, or an empty string if the file is empty.
     * @throws IOException If an I/O error occurs.
     */
    public static String readFirstLine(String filename) throws IOException {
        try (var lines = Files.lines(Path.of(filename))) {
            return lines.findFirst().orElse("");
        }
    }

    /**
     * Writes a string to a file, stripping any leading or trailing whitespace.
     *
     * @param filename The name of the file to write to.
     * @param content  The string content to write.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeString(String filename,  String content) throws IOException {
        Files.writeString(Path.of(filename), content.strip());
    }

    /**
     * Reads the content of a file as a string and converts it to a byte array.
     *
     * @param filename The name of the file to read from.
     * @return A byte array representing the file content.
     * @throws IOException If an I/O error occurs.
     */
    public static byte  [] readStringAsBytes(String filename) throws IOException {
        String content = Files.readString(Path.of(filename)).trim();
        return CryptographyUtils.hexStringToByteArray(content);
    }

    /**
     * Writes a byte array to a file as a hexadecimal string.
     *
     * @param filename The name of the file to write to.
     * @param data     The byte array to write.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeBytesAsHex(String filename, byte[] data) throws IOException {
        String hex = bytesToHex(data);
        writeString(filename, hex);
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return A string representing the byte array in hexadecimal format.
     */
    private static  String bytesToHex(byte  [] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}