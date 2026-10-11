import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class Main {
    private static final int REPEATS = 7;
    private static volatile int checksum;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 20; i++) {
            int[] keys = generateKeys(1000, i);
            measureBST(keys, keys);
            measureHashTable(keys, keys);
        }

        Files.createDirectories(Path.of("results"));
        try (PrintWriter output = new PrintWriter("results/results.csv")) {
            String header = "order,n,structure,insert_ms,search_ms,delete_ms";
            output.println(header);
            System.out.println(header);

            for (int n : new int[]{100, 1000, 10000}) {
                int[] randomKeys = generateKeys(n, 42 + n);
                int[] queries = randomKeys.clone();
                shuffle(queries, new Random(99 + n));
                int[] sortedKeys = randomKeys.clone();
                Arrays.sort(sortedKeys);

                benchmark("random", randomKeys, queries, output);
                benchmark("sorted", sortedKeys, queries, output);
            }
        }
        System.out.println("Saved results/results.csv");
        System.out.println("Checksum: " + checksum);
    }

    private static void benchmark(String order, int[] keys, int[] queries,
                                  PrintWriter output) {
        long[][] bstTimes = new long[REPEATS][];
        long[][] hashTimes = new long[REPEATS][];
        for (int run = 0; run < REPEATS; run++) {
            if (run % 2 == 0) {
                bstTimes[run] = measureBST(keys, queries);
                hashTimes[run] = measureHashTable(keys, queries);
            } else {
                hashTimes[run] = measureHashTable(keys, queries);
                bstTimes[run] = measureBST(keys, queries);
            }
        }
        printResult(order, keys.length, "BST", bstTimes, output);
        printResult(order, keys.length, "HashTable", hashTimes, output);
    }

    private static long[] measureBST(int[] keys, int[] queries) {
        BST tree = new BST();
        int inserted = 0;
        int found = 0;
        int deleted = 0;

        long start = System.nanoTime();
        for (int key : keys) {
            if (tree.insert(key)) inserted++;
        }
        long afterInsert = System.nanoTime();
        for (int key : queries) {
            if (tree.search(key)) found++;
        }
        long afterSearch = System.nanoTime();
        for (int key : queries) {
            if (tree.delete(key)) deleted++;
        }
        long afterDelete = System.nanoTime();
        verifyCounts(keys.length, inserted, found, deleted);
        return new long[]{afterInsert - start, afterSearch - afterInsert,
                          afterDelete - afterSearch};
    }

    private static long[] measureHashTable(int[] keys, int[] queries) {
        HashTable table = new HashTable(2 * keys.length + 1);
        int inserted = 0;
        int found = 0;
        int deleted = 0;

        long start = System.nanoTime();
        for (int key : keys) {
            if (table.insert(key)) inserted++;
        }
        long afterInsert = System.nanoTime();
        for (int key : queries) {
            if (table.search(key)) found++;
        }
        long afterSearch = System.nanoTime();
        for (int key : queries) {
            if (table.delete(key)) deleted++;
        }
        long afterDelete = System.nanoTime();
        verifyCounts(keys.length, inserted, found, deleted);
        return new long[]{afterInsert - start, afterSearch - afterInsert,
                          afterDelete - afterSearch};
    }

    private static void verifyCounts(int n, int inserted, int found, int deleted) {
        if (inserted != n || found != n || deleted != n) {
            throw new IllegalStateException("Incorrect benchmark result");
        }
        checksum += inserted + found + deleted;
    }

    private static int[] generateKeys(int n, long seed) {
        Random random = new Random(seed);
        Set<Integer> used = new HashSet<>();
        int[] keys = new int[n];
        int count = 0;
        while (count < n) {
            int key = random.nextInt(n * 100);
            if (used.add(key)) {
                keys[count++] = key;
            }
        }
        return keys;
    }

    private static void shuffle(int[] keys, Random random) {
        for (int i = keys.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = keys[i];
            keys[i] = keys[j];
            keys[j] = temp;
        }
    }

    private static void printResult(String order, int n, String structure,
                                    long[][] times, PrintWriter output) {
        double[] medians = new double[3];
        for (int operation = 0; operation < 3; operation++) {
            long[] values = new long[REPEATS];
            for (int run = 0; run < REPEATS; run++) {
                values[run] = times[run][operation];
            }
            Arrays.sort(values);
            medians[operation] = values[REPEATS / 2] / 1_000_000.0;
        }
        String line = String.format(Locale.US, "%s,%d,%s,%.6f,%.6f,%.6f",
                order, n, structure, medians[0], medians[1], medians[2]);
        output.println(line);
        System.out.println(line);
    }
}
