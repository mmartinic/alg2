import java.util.*;

public class BurrowsWheeler {

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);

        int first = -1;
        char[] lastColumn = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            int offset = circularSuffixArray.index(i);

            if (offset == 0) {
                first = i;
            }

            char c = s.charAt((offset + s.length() - 1) % s.length());
            lastColumn[i] = c;
        }

        BinaryStdOut.write(first);
        for (int i = 0; i < lastColumn.length; i++) {
            char c = lastColumn[i];
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {

        int first = BinaryStdIn.readInt();
        String lastColumnString = BinaryStdIn.readString();
        char[] lastColumn = lastColumnString.toCharArray();

        decode(first, lastColumn);
    }

    private static void decode(int first, char[] lastColumn) {
        char[] firstColumn = Arrays.copyOf(lastColumn, lastColumn.length);
        Arrays.sort(firstColumn);

        int[] next = new int[lastColumn.length];

        Map<Character, List<Integer>> lastRowChars = new HashMap<>();
        for (int i = 0; i < lastColumn.length; i++) {
            char c = lastColumn[i];

            List<Integer> lastRowCharIndices = lastRowChars.get(c);
            if (lastRowCharIndices == null) {
                lastRowCharIndices = new ArrayList<>();
                lastRowChars.put(c, lastRowCharIndices);
            }
            lastRowCharIndices.add(i);
        }

        for (int i = 0; i < firstColumn.length; ) {
            char c = firstColumn[i];
            List<Integer> lastRowCharIndices = lastRowChars.get(c);

            for (int j = 0; j < lastRowCharIndices.size(); j++) {
                next[i++] = lastRowCharIndices.get(j);
            }
        }

        for (int x = first, i = 0; i < lastColumn.length; x = next[x], i++) {
            BinaryStdOut.write(firstColumn[x]);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {

//        decode(3, "ARD!RCAAAABB".toCharArray());

        if (args.length == 1 && args[0].equals("-")) {
            encode();
        } else if (args.length == 1 && args[0].equals("+")) {
            decode();
        } else {
            StdOut.println("Usage: BurrowsWheeler - +");
        }
    }
}
