public class CircularSuffixArray {

    private static final int R = 256;
    private static final int CUTOFF = 15;   // cutoff to insertion sort

    private int[] offsets;
    private String s;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        this.s = s;
        offsets = new int[s.length()];

        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = i;
        }

        q3sort();
    }

    // msd
    private void msdSort() {
        final int N = offsets.length;
        int[] aux = new int[N];
        msdSort(0, N, 0, aux);
    }

    // q3string
    private void q3sort() {
        final int N = offsets.length;
        q3sort(0, N, 0);
    }

    private void msdSort(int start, int end, int d, int[] aux) {

        if (end - start < CUTOFF) {
            insertion(start, end, d);
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
            msdSort(start + count[i], start + count[i + 1], d + 1, aux);
        }
    }

    private void insertion(int start, int end, int d) {
        for (int i = start; i < end; i++) {
            for (int j = i; j > start && less(j, j - 1, d); j--) {
                exch(j, j - 1);
            }
        }
    }

    private boolean less(int x1, int x2, int d) {
        for (int i = d; i < s.length(); i++) {
            char c1 = chatAt(offsets[x1], i);
            char c2 = chatAt(offsets[x2], i);
            if (c1 < c2) {
                return true;
            } else if (c1 > c2) {
                return false;
            }
        }
        return false;
    }

    private void exch(int x1, int x2) {
        int temp = offsets[x1];
        offsets[x1] = offsets[x2];
        offsets[x2] = temp;
    }

    private void q3sort(int start, int end, int d) {
        if (end - start < CUTOFF) {
            insertion(start, end, d);
            return;
        }

//        if (start >= end - 1) {
//            return;
//        }

        int lt = start;
        int gt = end - 1;
        int i = start + 1;
        int v = chatAt(offsets[start], d);

        while (i <= gt) {
            char c = chatAt(offsets[i], d);

            if (c < v) {
                exch(i++, lt++);
            } else if (c > v) {
                exch(i, gt--);
            } else {
                i++;
            }
        }

        q3sort(start, lt, d);
        q3sort(lt, gt + 1, d + 1);
        q3sort(gt + 1, end, d);
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

        In in = new In("D:\\myProjects\\coursera\\alg2\\week6\\burrows\\aesop.txt");

        Stopwatch stopwatch = new Stopwatch();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(in.readAll());
        System.out.println(stopwatch.elapsedTime());

//        for (int i = 0; i < circularSuffixArray.length(); i++) {
//            System.out.println(circularSuffixArray.index(i));
//        }
    }
}
