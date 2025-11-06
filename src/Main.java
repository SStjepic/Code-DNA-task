import ziptool.ZipAnalyzer;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static ZipAnalyzer analyzer = new ZipAnalyzer();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            menu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    analyze();
                    break;
                case 2:
                    break;
                case 3:
                    exit = true;
                    break;
            }
        }

    }

    public static void menu(){
        System.out.println("===============================================");
        System.out.println("Menu");
        System.out.println("1. Analyze .zip/.jar file");
        System.out.println("2. Compare two .zip/.jar files");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void analyze(){
        System.out.print("Enter a absolute path to the first .zip archive: ");
        String firstZipPath = scanner.nextLine();

        try {
            String outputPath = analyzer.analyze(firstZipPath);
            System.out.println("Output saved to: " + outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}