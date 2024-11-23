import parts.Parte1;
import parts.Parte2;

public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Executar Parte1? (S/N)");
    if (scanner.nextLine().equalsIgnoreCase("S")) Parte1.generateKeysAndSaveToFile();

    System.out.println("Executar Parte2? (S/N)");
    if (scanner.nextLine().equalsIgnoreCase("S")) Parte2.processMessage();
}
