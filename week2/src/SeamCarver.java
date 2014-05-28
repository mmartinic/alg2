import java.awt.*;

public class SeamCarver {

    private Energy energy;
    private Colors colors;

    public SeamCarver(Picture picture) {
        colors = new Colors(picture);
        initEnergy();
    }

    // current picture
    public Picture picture() {
        return colors.picture();
    }

    // width  of current picture
    public int width() {
        return colors.width();
    }

    // height of current picture
    public int height() {
        return colors.height();
    }

    // energy of pixel at column x and row y in current picture
    public double energy(int x, int y) {
        return energy.get(x, y);
    }

    private void setTranspose(boolean trans) {
        colors.setTranspose(trans);
        energy.setTranspose(trans);
    }

    private void initEnergy() {

        energy = new Energy(colors.width(), colors.height(), colors.getTranspose());

        for (int x = 0; x < energy.width(); x++) {
            for (int y = 0; y < energy.height(); y++) {
                energy.set(x, y, getGradient(x, y));
            }
        }
    }

    private double getGradient(int x, int y) {
        if (x == 0 || x == energy.width() - 1 || y == 0 || y == energy.height() - 1) {
            return 195075;
        }

        double dx = getSingleGradient(x + 1, y, x - 1, y);
        double dy = getSingleGradient(x, y + 1, x, y - 1);
        return dx + dy;
    }

    private double getSingleGradient(int x1, int y1, int x2, int y2) {

        Color color1 = colors.getColor(x1, y1);
        Color color2 = colors.getColor(x2, y2);

        double dr = color1.getRed() - color2.getRed();
        double dg = color1.getGreen() - color2.getGreen();
        double db = color1.getBlue() - color2.getBlue();

        double d = dr * dr + dg * dg + db * db;

        return d;
    }

    // sequence of indices for horizontal seam in current picture
    public int[] findHorizontalSeam() {
        setTranspose(true);
        int[] seam = findSeam();
        setTranspose(false);
        return seam;
    }

    // sequence of indices for vertical seam in current picture
    public int[] findVerticalSeam() {
        return findSeam();
    }

    private int[] findSeam() {

        int width = energy.width();
        int height = energy.height();

        int[][] parent = new int[width][height];
        double[][] distTo = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 0; x < width; x++) {
            distTo[x][0] = energy.get(x, 0);
        }

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                relax(x, y, x - 1, y + 1, distTo, parent);
                relax(x, y, x, y + 1, distTo, parent);
                relax(x, y, x + 1, y + 1, distTo, parent);
            }
        }

        double sinkDistance = Double.POSITIVE_INFINITY;
        int sinkParent = 0;

        for (int x = 0; x < width; x++) {
            if (distTo[x][height - 1] < sinkDistance) {
                sinkDistance = distTo[x][height - 1];
                sinkParent = x;
            }
        }

        int[] verticalSeam = new int[height];
        verticalSeam[height - 1] = sinkParent;
        for (int y = height - 2; y >= 0; y--) {
            verticalSeam[y] = parent[verticalSeam[y + 1]][y + 1];
        }

        return verticalSeam;
    }

    private void relax(int x1, int y1, int x2, int y2, double[][] distTo, int[][] parent) {
        if (x2 < 0 || x2 >= distTo.length) {
            return;
        }

        if (distTo[x1][y1] + energy.get(x2, y2) < distTo[x2][y2]) {
            distTo[x2][y2] = distTo[x1][y1] + energy.get(x2, y2);
            parent[x2][y2] = x1;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] a) {
        setTranspose(true);
        removeSeam(a);
        setTranspose(false);
        initEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] a) {
        removeSeam(a);
        initEnergy();
    }

    private void removeSeam(int[] a) {

        Colors newColors = new Colors(colors.width() - 1, colors.height(), colors.getTranspose());
        for (int y = 0; y < colors.height(); y++) {
            int newX = 0;
            for (int x = 0; x < colors.width(); x++) {
                if (a[y] != x) {
                    newColors.set(newX++, y, colors.get(x, y));
                }
            }
        }
        colors = newColors;
    }

    public static void main(String[] args) {
        Picture org = new Picture("D:\\myProjects\\coursera\\alg2\\week2\\seamCarving\\HJocean.png");
        org.show();

        SeamCarver sc = new SeamCarver(org);
        Picture picture = sc.picture();
        picture.show();

    }
}