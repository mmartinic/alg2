import java.awt.Color;

public class Colors {

    private final int[] c;
    private int width;
    private int height;
    private boolean trans;

    public Colors(int width, int height, boolean trans) {
        this.width = trans == false ? width : height;
        this.height = trans == false ? height : width;
        this.trans = trans;
        c = new int[this.width * this.height];
    }

    public Colors(Picture picture) {
        this(picture.width(), picture.height(), false);

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                Color color = picture.get(x, y);
                set(x, y, color);
            }
        }
    }

    public int width() {
        return trans == false ? width : height;
    }

    public int height() {
        return trans == false ? height : width;
    }

    public void set(int x, int y, int color) {
        c[getIndex(x, y)] = color;
    }

    public void set(int x, int y, Color color) {
        set(x, y, color.getRGB());
    }

    public int get(int x, int y) {
        return c[getIndex(x, y)];
    }

    public Color getColor(int x, int y) {
        int value = get(x, y);
        return new Color(value);
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

    public Picture picture() {
        Picture picture = new Picture(width(), height());

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                picture.set(getX(x, y), getY(x, y), getColor(x, y));
            }
        }

        return picture;
    }

    public void setTranspose(boolean trans) {
        this.trans = trans;
    }

    public boolean getTranspose() {
        return trans;
    }
}
