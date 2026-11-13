import java.util.*;
public class KMP {
    private static int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
    public static List<Integer> search(
            String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        List<Integer> positions = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        int[] lps = buildLPS(pattern);
        int i = 0;
        int j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }
            if (j == m) {
                positions.add(i - j);
                j = lps[j - 1];
            } else if (i < n &&
                    text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return positions;
    }
    public static List<AhoCorasick.Match> searchAll(String text, List<String> patterns) {
        List<AhoCorasick.Match> matches = new ArrayList<>();
        for (int id = 0; id < patterns.size(); id++) {
            List<Integer> positions = search(text, patterns.get(id));
            for (int position : positions) {
                matches.add(new AhoCorasick.Match(id, position));
            }
        }
        return matches;
    }
    public static void main(String[] args) {
        String text = "ababcabcabababd";
        String pattern = "ababd";
        List<Integer> results = search(text, pattern);
        System.out.println("Text: " + text);
        System.out.println("Pattern: " + pattern);
        System.out.println("Matches: " + results);
    }
}