public class CircularSuffixArray {

    private static final int R = 256;

    private int[] offsets;
    private String s;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        this.s = s;
        offsets = new int[s.length()];

        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = i;
        }

        sort();
    }

    // msd
    private void sort() {
        final int W = s.length();
        final int N = offsets.length;
        int[] aux = new int[N];
        sort(0, N, 0, aux, W);
    }

    private void sort(int start, int end, int d, int[] aux, int W) {

        if (d == W - 1 || start >= end - 1) {
            return;
        }

        int[] count = new int[R + 1];
        for (int i = start; i < end; i++) {
            char c = chatAt(offsets[i], d);
            count[c + 1]++;
        }

        for (int i = 0; i < count.length - 1; i++) {
            count[i + 1] = count[i] + count[i + 1];
        }

        for (int i = start; i < end; i++) {
            char c = chatAt(offsets[i], d);
            aux[count[c]++] = offsets[i];
        }

        for (int i = start; i < end; i++) {
            offsets[i] = aux[i - start];
        }

        for (int i = 0; i < R; i++) {
            sort(start + count[i], start + count[i + 1], d + 1, aux, W);
        }

    }

    private char chatAt(int offset, int d) {
        return s.charAt((offset + d) % s.length());
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return offsets[i];
    }

    public static void main(String[] args) {
        String s = "ABRACADABRA!";

        In in = new In("D:\\myProjects\\coursera\\alg2\\week6\\burrows\\amendments.txt");

        Stopwatch stopwatch = new Stopwatch();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(in.readAll());
        System.out.println(stopwatch.elapsedTime());

//        for (int i = 0; i < circularSuffixArray.length(); i++) {
//            System.out.println(circularSuffixArray.index(i));
//        }
    }
}
