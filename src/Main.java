import tool.ZipAnalyzer;
import tool.FileComparator;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static ZipAnalyzer analyzer = new ZipAnalyzer();
    public static FileComparator fileComparator = new FileComparator();

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
                    compare();
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
        System.out.println("2. Compare two .txt files files");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void analyze(){
        System.out.print("Enter a absolute path to the .zip archive: ");
        String zipPath = scanner.nextLine();

        try {
            String outputPath = analyzer.analyze(zipPath);
            System.out.println("Output saved to: " + outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void compare(){
        System.out.print("Enter a absolute path to the first .txt file: ");
        String firstZipPath = scanner.nextLine();
        System.out.print("Enter a absolute path to the second .txt file: ");
        String secondZipPath = scanner.nextLine();

        try {
            double similarity =  fileComparator.compare(firstZipPath, secondZipPath);
            System.out.println("Similarity: " + String.format("%.2f%%", similarity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}