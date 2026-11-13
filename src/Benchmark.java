import java.util.*;
import java.io.*;
public class Benchmark {
    private static final int[] PATTERN_COUNTS = {10, 100, 1000};
    private static final int[] TEXT_LENGTHS = {1000, 10000, 100000};
    private static final int RUNS = 5;
    private static final Random random = new Random(42);
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static String generateText(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
            sb.append(c);
        }
        return sb.toString();
    }
    private static List<String> generatePatterns(int count) {
        List<String> patterns = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int length = 3 + random.nextInt(8);
            patterns.add(generateText(length));
        }
        return patterns;
    }
    private static List<String> normalize(List<AhoCorasick.Match> matches) {
        List<String> result = new ArrayList<>();
        for (AhoCorasick.Match match : matches) {
            result.add(match.patternId + ":" + match.startIndex);
        }
        Collections.sort(result);
        return result;
    }
    private static void correctnessTest() {
        List<String> patterns = Arrays.asList("he", "she", "his", "hers");
        String text = "ahishers";
        AhoCorasick ac = new AhoCorasick(patterns);
        List<String> ahoResults = normalize(ac.search(text));
        List<String> kmpResults = normalize(KMP.searchAll(text, patterns));
        if (!ahoResults.equals(kmpResults) || ahoResults.size() != 4) {
            throw new AssertionError("Correctness test failed!");
        }
        System.out.println("Correctness test passed!");
    }
    public static void main(String[] args) throws IOException {
        correctnessTest();
        PrintWriter writer = new PrintWriter(new FileWriter("results.csv"));
        writer.println("Patterns,TextLength,AhoMs,KmpMs,Matches");
        System.out.printf("%-12s %-12s %-15s %-15s %-10s%n", "Patterns", "TextLength", "Aho (ms)", "KMP (ms)", "Matches");
        String warmupText = generateText(10000);
        List<String> warmupPatterns = generatePatterns(100);
        for (int i = 0; i < 10; i++) {
            AhoCorasick warmupAC = new AhoCorasick(warmupPatterns);
            warmupAC.search(warmupText);
            KMP.searchAll(warmupText, warmupPatterns);
        }
        for (int patternCount : PATTERN_COUNTS) {
            for (int textLength : TEXT_LENGTHS) {
                String text = generateText(textLength);
                List<String> patterns = generatePatterns(patternCount);
                long totalAhoTime = 0;
                long totalKmpTime = 0;
                int matchCount = 0;
                for (int run = 0; run < RUNS; run++) {
                    long startAho = System.nanoTime();
                    AhoCorasick ac = new AhoCorasick(patterns);
                    List<AhoCorasick.Match> ahoMatches = ac.search(text);
                    long endAho = System.nanoTime();
                    totalAhoTime += endAho - startAho;
                    long startKmp = System.nanoTime();
                    List<AhoCorasick.Match> kmpMatches = KMP.searchAll(text, patterns);
                    long endKmp = System.nanoTime();
                    totalKmpTime += endKmp - startKmp;
                    if (!normalize(ahoMatches).equals(normalize(kmpMatches))) {
                        throw new AssertionError("Results do not match!");
                    }
                    matchCount = ahoMatches.size();
                }
                double avgAhoMs = totalAhoTime / (double) RUNS / 1_000_000.0;
                double avgKmpMs = totalKmpTime / (double) RUNS / 1_000_000.0;
                System.out.printf(
                        Locale.US,
                        "%-12d %-12d %-15.3f %-15.3f %-10d%n",
                        patternCount,
                        textLength,
                        avgAhoMs,
                        avgKmpMs,
                        matchCount
                );
                writer.printf(
                        Locale.US,
                        "%d,%d,%.3f,%.3f,%d%n",
                        patternCount,
                        textLength,
                        avgAhoMs,
                        avgKmpMs,
                        matchCount
                );
            }
        }
        writer.close();
        System.out.println("\nResults saved to results.csv");
    }
}