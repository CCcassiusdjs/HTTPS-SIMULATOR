package parts;

import config.Constants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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
            System.out.println("Arquivos 'a.txt' e 'AA.txt' com valores presentes! Usar valores existentes? (S/N)");
            if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
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

        // Salvando as chaves ao final do cálculo
        BigInteger a = futureA.get();
        BigInteger AA = futureAA.get();

        CompletableFuture.runAsync(() -> saveValueToFile("a.txt", a.toString(16)));
        CompletableFuture.runAsync(() -> saveValueToFile("AA.txt", AA.toString(16)));

        System.out.println("Valor de 'a': " + a.toString(16));
        System.out.println("Valor de 'AA': " + AA.toString(16));
    }

    /**
     * Gera a chave privada de forma segura.
     *
     * @return Chave privada gerada.
     */
    private static @NotNull BigInteger generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(Constants.P.bitLength() - 1, random);
    }

    /**
     * Lê um valor BigInteger de um arquivo.
     *
     * @param filename Nome do arquivo.
     * @return Valor lido como BigInteger.
     */
    @Contract("_ -> new")
    private static @NotNull BigInteger loadValueFromFile(String filename) {
        try {
            return new BigInteger(FileUtils.readFirstLine(filename), 16);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar valor do arquivo: " + filename, e);
        }
    }

    /**
     * Salva um valor em um arquivo.
     *
     * @param filename Nome do arquivo.
     * @param value    Valor a ser salvo.
     */
    private static void saveValueToFile(String filename, String value) {
        try {
            FileUtils.writeString(filename, value);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar valor no arquivo: " + filename, e);
        }
    }
}
