public class Energy {

    private final double[][] e;
    private int width;
    private int height;
    private boolean trans;

    public Energy(int width, int height) {
        this(width, height, false);
    }

    public Energy(int width, int height, boolean trans) {
        setWidth(width);
        setHeight(height);
        this.trans = trans;
        e = new double[this.width][this.height];
    }

    public int width() {
        return trans == false ? width : height;
    }

    public int height() {
        return trans == false ? height : width;
    }

    public void setWidth(int width) {
        this.width = trans == false ? width : height;
    }

    public void setHeight(int height) {
        this.height = trans == false ? height : width;
    }

    public void set(int x, int y, double value) {
        e[getX(x, y)][getY(x, y)] = value;
    }

    public double get(int x, int y) {
        return e[getX(x, y)][getY(x, y)];
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
