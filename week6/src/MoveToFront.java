public class MoveToFront {

    private static final int R = 256;

    private static class Node {
        private Node previous;
        private Node next;
        private char c;
    }

    private static class NodeResult {
        private int i;
        private Node node;
    }

    private static class List {
        private Node root;

        public NodeResult get(char c) {
            int i = 0;
            Node node = root;
            while (node != null) {
                if (node.c == c) {
                    NodeResult nodeResult = new NodeResult();
                    nodeResult.i = i;
                    nodeResult.node = node;
                    return nodeResult;
                }
                node = node.next;
                i++;
            }
            return null;
        }

        public NodeResult get(int index) {
            int i = 0;
            Node node = root;
            while (node != null) {
                if (index == i) {
                    NodeResult nodeResult = new NodeResult();
                    nodeResult.i = i;
                    nodeResult.node = node;
                    return nodeResult;
                }
                node = node.next;
                i++;
            }
            return null;
        }

        public void add(char c) {
            Node node = new Node();
            node.c = c;
            node.next = root;
            if (root != null) {
                root.previous = node;
            }
            root = node;
        }

        public void moveToFront(Node node) {

            if (node == root) {
                return;
            }

            if (node.previous != null) {
                node.previous.next = node.next;
            }

            if (node.next != null) {
                node.next.previous = node.previous;
            }

            node.next = root;
            node.previous = null;
            if (root != null) {
                root.previous = node;
            }
            root = node;
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        List chars = initChars();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            NodeResult nodeResult = chars.get(c);
            BinaryStdOut.write(nodeResult.i, 8);

            chars.moveToFront(nodeResult.node);
        }

        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        List chars = initChars();

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readInt(8);
            NodeResult nodeResult = chars.get(i);
            BinaryStdOut.write(nodeResult.node.c);

            chars.moveToFront(nodeResult.node);
        }

        BinaryStdOut.flush();
    }

    private static List initChars() {
        List chars = new List();
        for (int i = R - 1; i >= 0; i--) {
            chars.add((char) i);
        }
        return chars;
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("-")) {
            encode();
        } else if (args.length == 1 && args[0].equals("+")) {
            decode();
        } else {
            StdOut.println("Usage: MoveToFront - +");
        }
    }
}
