package tool;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipAnalyzer {
    /**
     * Analyzes a ZIP file and creates a .txt file containing its contents.
     *
     * @param zipPath the absolute path to the ZIP file
     * @return the path to the generated .txt file
     * @throws IOException if the file does not exist or an I/O error occurs during reading or writing
     */

    public String analyze(String zipPath) throws IOException {
        Path zipFile = Paths.get(zipPath);
        if (!Files.exists(zipFile)) {
            throw new FileNotFoundException("Zip file not found: " + zipPath);
        }
        String fileNameWithoutExtension = zipFile.getFileName().toString().replaceFirst("[.][^.]+$", "");
        Path outputFile = zipFile.getParent().resolve(fileNameWithoutExtension + ".txt");

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile));
             BufferedWriter writer = Files.newBufferedWriter(outputFile)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if(entry.getName().endsWith(".class")) {
                    writer.write(entry.getName().toLowerCase());
                    writer.newLine();
                }
            }
            return outputFile.toAbsolutePath().toString();
        }
    }
}