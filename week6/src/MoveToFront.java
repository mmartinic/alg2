import java.util.ArrayList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        ArrayList<Character> chars = initChars();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int i = chars.indexOf(c);
            BinaryStdOut.write(i, 8);

            chars.remove(i);
            chars.add(0, c);
        }

        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        ArrayList<Character> chars = initChars();

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readInt(8);
            char c = chars.get(i);
            BinaryStdOut.write(c);

            chars.remove(i);
            chars.add(0, c);
        }

        BinaryStdOut.flush();
    }

    private static ArrayList<Character> initChars() {
        ArrayList<Character> chars = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
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
