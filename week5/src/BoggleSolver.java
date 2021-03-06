import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private static final int R = 'Z' - 'A' + 1;

    private final Trie trie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new Trie();
        for (int i = 0; i < dictionary.length; i++) {
            String s = dictionary[i];
            trie.add(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        Set<String> validWords = new HashSet<>();
        boolean[][] marked = new boolean[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(board, "", trie.root, validWords, marked, i, j);
            }
        }
        return validWords;
    }

    private void dfs(final BoggleBoard board, final String prefix, final Node parentNode, final Set<String> validWords,
                     final boolean[][] marked, final int i, final int j) {
        char letter = board.getLetter(i, j);
        String word = prefix + letter;

        if (letter == 'Q') {
            word = word + 'U';
        }

        Node node = trie.get(parentNode, word, prefix.length());
        if (node == null) {
            return;
        }

        marked[i][j] = true;

        if (node.isWord && word.length() > 2) {
            validWords.add(word);
        }

        //adj
        for (int k = i - 1; k <= i + 1; k++) {
            if (k < 0 || k >= board.rows()) {
                continue;
            }

            for (int l = j - 1; l <= j + 1; l++) {
                if (k == i && l == j) {
                    continue;
                }

                if (l < 0 || l >= board.cols()) {
                    continue;
                }

                if (!marked[k][l]) {
                    dfs(board, word, node, validWords, marked, k, l);
                }
            }
        }

        marked[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        boolean contains = trie.contains(word);

        if (contains) {
            int length = word.length();

            if (length >= 0 && length <= 2) {
                return 0;
            }
            if (length == 3 || length == 4) {
                return 1;
            } else if (length == 5) {
                return 2;
            } else if (length == 6) {
                return 3;
            } else if (length == 7) {
                return 5;
            } else if (length >= 8) {
                return 11;
            }
        }
        return 0;
    }

    private static class Node {
        private boolean isWord;
        private Node[] next = new Node[R];
    }

    private static class Trie {

        private Node root;

        public void add(String s) {
            root = add(root, s, 0);
        }

        private Node add(Node node, String s, int d) {
            if (node == null) {
                node = new Node();
            }

            if (s.length() == d) {
                node.isWord = true;
            } else {
                char c = s.charAt(d);
                node.next[c - 'A'] = add(node.next[c - 'A'], s, d + 1);

            }
            return node;
        }

        public Node get(String s) {
            return get(root, s, 0);
        }

        private Node get(Node node, String s, int d) {
            if (node == null) {
                return null;
            }

            if (s.length() == d) {
                return node;
            }

            char c = s.charAt(d);
            return get(node.next[c - 'A'], s, d + 1);
        }

        public boolean contains(String s) {
            Node node = get(s);
            return node != null && node.isWord;
        }
    }

    public static void main(String[] args) {
        In in = new In("D:\\myProjects\\coursera\\alg2\\week5\\boggle\\dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);

        BoggleBoard board = new BoggleBoard("D:\\myProjects\\coursera\\alg2\\week5\\boggle\\board-aqua.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
