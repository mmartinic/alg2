import java.awt.Color;

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
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] a) {
        removeSeam(a);
    }

    private void removeSeam(int[] a) {

        if (colors.height() <= 1 || colors.width() <= 1) {
            throw new IllegalArgumentException();
        }

        if (a.length != colors.height()) {
            throw new IllegalArgumentException();
        }

        if (a[0] < 0 || a[0] >= colors.width()) {
            throw new IllegalArgumentException();
        }

        for (int y = 0; y < a.length - 1; y++) {
            int x1 = a[y];
            int x2 = a[y + 1];
            if (x2 < 0 || x2 >= colors.width()) {
                throw new IllegalArgumentException();
            }

            if (Math.abs(x2 - x1) > 1) {
                throw new IllegalArgumentException();
            }
        }

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

        Energy newEnergy = new Energy(energy.width() - 1, energy.height(), colors.getTranspose());
        for (int y = 0; y < energy.height(); y++) {
            int newX = 0;
            for (int x = 0; x < energy.width(); x++) {
                if (a[y] != x) {
                    newEnergy.set(newX++, y, energy.get(x, y));
                }
            }
        }
        energy = newEnergy;

        for (int y = 0; y < energy.height(); y++) {
            int seamX = a[y];

            if (seamX == colors.width()) {
                double gradient = getGradient(seamX - 1, y);
                energy.set(seamX - 1, y, gradient);
            } else {
                double gradient = getGradient(seamX, y);
                energy.set(seamX, y, gradient);

                if (seamX > 0) {
                    gradient = getGradient(seamX - 1, y);
                    energy.set(seamX - 1, y, gradient);
                }
            }
        }
    }
}