import parts.Parte1;
import parts.Parte2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Execute PART I? (Y/N)");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            Parte1.generateKeysAndSaveToFile();
        }

        System.out.println("Execute PART II? (Y/N)");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            Parte2.processMessage();
        }
    }
}
