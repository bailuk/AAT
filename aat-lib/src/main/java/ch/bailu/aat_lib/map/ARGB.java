package ch.bailu.aat_lib.map;

public class ARGB {
    public int a,r,g,b;

    public ARGB(int color) {
        a = (color >> 24) & 0xff;
        r = (color >> 16) & 0xff;
        g = (color >> 8) & 0xff;
        b = (color) & 0xff;
    }

    public ARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int toInt() {
        int result = (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
        return result;
    }
}
