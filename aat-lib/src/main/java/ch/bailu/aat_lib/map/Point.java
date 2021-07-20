package ch.bailu.aat_lib.map;

public class Point {

    public int x, y;

    public Point(int x2, int y2) {
        x = x2;
        y = y2;
    }

    public Point() {
        x=y=0;
    }

    public Point(float x2, float y2) {
        x = Math.round(x);
        y = Math.round(y);
    }

    public void set(Point p) {
        x = p.x;
        y = p.y;
    }
}
