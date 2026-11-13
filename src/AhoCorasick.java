import java.util.*;
public class AhoCorasick {
    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node failure;
        Node outputLink;
        List<Integer> patternIds = new ArrayList<>();
    }
    public static class Match {
        public final int patternId;
        public final int startIndex;
        public Match(int patternId, int startIndex) {
            this.patternId = patternId;
            this.startIndex = startIndex;
        }
        @Override
        public String toString() {
            return "Pattern " + patternId +
                    " at index " + startIndex;
        }
    }
    private final Node root = new Node();
    private final List<String> patterns;
    public AhoCorasick(List<String> patterns) {
        this.patterns = new ArrayList<>(patterns);
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i) == null || patterns.get(i).isEmpty()) {
                throw new IllegalArgumentException("Patterns cannot be null or empty");
            }
            insert(patterns.get(i), i);
        }
        buildFailureLinks();
    }
    private void insert(String pattern, int id) {
        Node current = root;
        for (char c : pattern.toCharArray()) {
            current.children.putIfAbsent(c, new Node());
            current = current.children.get(c);
        }
        current.patternIds.add(id);
    }
    private void buildFailureLinks() {
        Queue<Node> queue = new ArrayDeque<>();
        root.failure = root;
        for (Node child : root.children.values()) {
            child.failure = root;
            queue.add(child);
        }
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();
                Node fail = current.failure;
                while (fail != root && !fail.children.containsKey(c)) {
                    fail = fail.failure;
                }
                if (fail.children.containsKey(c)) {
                    child.failure = fail.children.get(c);
                } else {
                    child.failure = root;
                }
                if (!child.failure.patternIds.isEmpty()) {
                    child.outputLink = child.failure;
                } else {
                    child.outputLink = child.failure.outputLink;
                }
                queue.add(child);
            }
        }
    }
    public List<Match> search(String text) {
        List<Match> matches = new ArrayList<>();
        Node current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (current != root && !current.children.containsKey(c)) {
                current = current.failure;
            }
            if (current.children.containsKey(c)) {
                current = current.children.get(c);
            } else {
                current = root;
            }
            addMatches(current, i, matches);
            Node output = current.outputLink;
            while (output != null) {
                addMatches(output, i, matches);
                output = output.outputLink;
            }
        }
        return matches;
    }
    private void addMatches(
            Node node,
            int endIndex,
            List<Match> matches) {
        for (int id : node.patternIds) {
            int start = endIndex - patterns.get(id).length() + 1;
            matches.add(new Match(id, start));
        }
    }
    public static void main(String[] args) {
        List<String> patterns = Arrays.asList("he", "she", "his", "hers");
        String text = "ahishers";
        AhoCorasick ac = new AhoCorasick(patterns);
        List<Match> matches = ac.search(text);
        System.out.println("Text: " + text);
        System.out.println("Patterns: " + patterns);
        System.out.println("Matches:");
        for (Match match : matches) {
            System.out.println(patterns.get(match.patternId) + " found at index " + match.startIndex);
        }
    }
}