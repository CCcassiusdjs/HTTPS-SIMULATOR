/**
 * Parte1.java
 * <p>
 * This file implements Diffie-Hellman key generation, including the creation of private
 * and public values, and saving the results to files for later use in secure communication.
 * <p>
 * Authors:
 * - Cassio Silva (c.jones@edu.pucrs.br)
 * - Arthur Gil (a.gil@edu.pucrs.br)
 * <p>
 * Creation Date: 20/11/2024
 * Last Modified: 23/11/2024
 *
 */

package parts;

import config.Constants;

import utils.FileUtils;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public final class Parte1 {
    public static void generateKeysAndSaveToFile() throws Exception {
        Scanner scanner = new Scanner(System.in);
        Path pathA = Path.of("a.txt");
        Path pathAA = Path.of("AA.txt");

        CompletableFuture<BigInteger> futureA;
        CompletableFuture<BigInteger> futureAA;

        if (Files.exists(pathA) && Files.exists(pathAA)) {
            System.out.println("Files 'a.txt' and 'AA.txt' with values found! Use existing values? (Y/N)");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                futureA = CompletableFuture.supplyAsync(() -> loadValueFromFile("a.txt"));
                futureAA = CompletableFuture.supplyAsync(() -> loadValueFromFile("AA.txt"));
            } else {
                futureA = CompletableFuture.supplyAsync(Parte1::generatePrivateKey);
                futureAA = futureA.thenApplyAsync(a -> Constants.G.modPow(a, Constants.P));
            }
        } else {
            futureA = CompletableFuture.supplyAsync(Parte1::generatePrivateKey);
            futureAA = futureA.thenApplyAsync(a -> Constants.G.modPow(a, Constants.P));
        }

        // Saving the keys after computation
        BigInteger a = futureA.get();
        BigInteger AA = futureAA.get();

        CompletableFuture.runAsync(() -> saveValueToFile("a.txt", a.toString(16)));
        CompletableFuture.runAsync(() -> saveValueToFile("AA.txt", AA.toString(16)));

        System.out.println("Value of 'a': " + a.toString(16));
        System.out.println("Value of 'AA': " + AA.toString(16));
    }

    /**
     * Securely generates the private key.
     *
     * @return Generated private key.
     */
    private static  BigInteger generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(Constants.P.bitLength() - 1, random);
    }

    /**
     * Reads a BigInteger value from a file.
     *
     * @param filename Name of the file.
     * @return The value read as BigInteger.
     */
    private static  BigInteger loadValueFromFile(String filename) {
        try {
            return new BigInteger(FileUtils.readFirstLine(filename), 16);
        } catch (Exception e) {
            throw new RuntimeException("Error loading value from file: " + filename, e);
        }
    }

    /**
     * Saves a value to a file.
     *
     * @param filename Name of the file.
     * @param value    The value to be saved.
     */
    private static void saveValueToFile(String filename, String value) {
        try {
            FileUtils.writeString(filename, value);
        } catch (Exception e) {
            throw new RuntimeException("Error saving value to file: " + filename, e);
        }
    }
}