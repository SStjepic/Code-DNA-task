package tool;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class FileComparator {

    private long simHashValue(List<String> features) {
        int[] vector = new int[64];

        for (String feature : features) {
            long hash = hash64(feature);  // 64-bit hash

            for (int i = 0; i < 64; i++) {
                if (((hash >> i) & 1L) == 1) {
                    vector[i] += 1;
                } else {
                    vector[i] -= 1;
                }
            }
        }

        long simhash = 0L;
        for (int i = 0; i < 64; i++) {
            if (vector[i] > 0) simhash |= (1L << i);
        }

        return simhash;
    }

    private long hash64(String s) {
        final long FNV_64_PRIME = 0x100000001b3L;
        long hash = 0xcbf29ce484222325L;

        for (char c : s.toCharArray()) {
            hash ^= c;
            hash *= FNV_64_PRIME;
        }

        return hash;
    }

    private int hammingDistance(long hash1, long hash2) {
        return Long.bitCount(hash1 ^ hash2);
    }

    private double similarity(long hash1, long hash2) {
        int distance = hammingDistance(hash1, hash2);
        return (1 - distance / 64.0) * 100.0;
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

        long hash1 = simHashValue(file1);
        long hash2 = simHashValue(file2);

        return similarity(hash1, hash2);
    }
}
