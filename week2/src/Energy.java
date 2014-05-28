public class Energy {

    private final double[] e;
    private int width;
    private int height;
    private boolean trans;

    public Energy(int width, int height) {
        this(width, height, false);
    }

    public Energy(int width, int height, boolean trans) {
        this.width = trans == false ? width : height;
        this.height = trans == false ? height : width;
        this.trans = trans;
        e = new double[this.width * this.height];
    }

    public int width() {
        return trans == false ? width : height;
    }

    public int height() {
        return trans == false ? height : width;
    }

    public void set(int x, int y, double value) {
        e[getIndex(x, y)] = value;
    }

    public double get(int x, int y) {
        return e[getIndex(x, y)];
    }

    private int getIndex(int x, int y) {
        int realX = getX(x, y);
        int realY = getY(x, y);

        if (realX < 0 || realX >= width) {
            throw new IndexOutOfBoundsException();
        }

        if (realY < 0 || realY >= height) {
            throw new IndexOutOfBoundsException();
        }

        int index = height * realX + realY;
        return index;
    }

    private int getX(int x, int y) {
        return trans == false ? x : y;
    }

    private int getY(int x, int y) {
        return trans == false ? y : x;
    }

    public void setTranspose(boolean trans) {
        this.trans = trans;
    }
}
