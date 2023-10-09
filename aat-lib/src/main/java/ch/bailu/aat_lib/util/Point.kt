package ch.bailu.aat_lib.util;

/**
 * A point holding two integer coordinates.
 * To keep some code independent from UI libraries.
 */
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
        x = Math.round(x2);
        y = Math.round(y2);
    }

    public Point(double x2, double y2) {
        x = Math.round((float)x2);
        y = Math.round((float)y2);
    }

    public void set(Point p) {
        x = p.x;
        y = p.y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
