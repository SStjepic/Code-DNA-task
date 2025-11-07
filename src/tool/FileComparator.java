package tool;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class FileComparator {

    private final int fingerprintLength = 512;

    private byte[] simHashValue(List<String> features) {
        int[] vector = new int[fingerprintLength];
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        for (String line : features) {
            byte[] hash = digest.digest(line.getBytes(StandardCharsets.UTF_8));

            for (int b = 0; b < hash.length; b++) {
                for (int bit = 0; bit < 8; bit++) {
                    int pos = b * 8 + bit;
                    if (pos >= fingerprintLength) break;
                    boolean bitSet = ((hash[b] >> (7 - bit)) & 1) == 1;
                    vector[pos] += bitSet ? 1 : -1;
                }
            }
        }

        byte[] fingerprint = new byte[fingerprintLength];
        for (int i = 0; i < fingerprintLength; i++) {
            fingerprint[i] = (byte) (vector[i] > 0 ? 1 : 0);
        }

        return fingerprint;
    }

    public int hammingDistance(byte[] a, byte[] b) {
        int diff = 0;
        for (int i = 0; i < fingerprintLength; i++) {
            if (a[i] != b[i]) diff++;
        }
        return diff;
    }

    public double similarity(byte[] a, byte[] b) {
        int distance = hammingDistance(a, b);
        return (1 - (distance/(double)fingerprintLength)) * 100.0;
    }

    /**
     * Compares two .txt files generated from ZIP/JAR archives using SimHash
     * and calculates the similarity percentage.
     *
     * @param firstPath the path to the first .txt file
     * @param secondPath the path to the second .txt file
     * @return the similarity percentage between the two files (0-100)
     * @throws Exception if a file does not exist or an I/O error occurs during reading
     */
    public double compare(String firstPath, String secondPath) throws Exception {
        List<String> file1 = Files.readAllLines(Path.of(firstPath));
        List<String> file2 = Files.readAllLines(Path.of(secondPath));

        byte[] hash1 = simHashValue(file1);
        byte[] hash2 = simHashValue(file2);

        return similarity(hash1, hash2);
    }
}
