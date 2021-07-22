package ch.bailu.aat_lib.util.color;

public class ARGB implements ColorInterface {

    private int a,r,g,b;

    public ARGB(int color) {
        a = (color >> 24) & 0xff;
        r = red(color);
        g = green(color);
        b = blue(color);
    }

    public ARGB(int alpha, int color) {
        a = alpha;
        r = red(color);
        g = green(color);
        b = blue(color);
    }

    public ARGB(int r, int g, int b) {
        this(255,r,g,b);
    }

    public ARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }



    public static int red(int color) {
        return (color >> 16) & 0xff;
    }

    public static int green(int color) {
        return (color >> 8) & 0xff;
    }

    public static int blue(int color) {
        return (color) & 0xff;
    }

    @Override
    public int red() {
        return r;
    }

    @Override
    public int green() {
        return g;
    }

    @Override
    public int blue() {
        return b;
    }

    @Override
    public int alpha() {
        return a;
    }

    public int toInt() {
        int result = (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
        return result;
    }
}
