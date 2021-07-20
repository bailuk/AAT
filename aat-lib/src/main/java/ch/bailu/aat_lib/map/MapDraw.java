package ch.bailu.aat_lib.map;


import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Paint;

public interface MapDraw {
    int POINT_RADIUS=3;
    int MARGIN=5;

    Paint getGridPaint();
    Bitmap getNodeBitmap();


    void grid(Point center, int space);

    void vLine(int x);
    void hLine(int y);

    void point(Point pixel);
    void textTop(String text, int line);

    void textBottom(String s, int line);
    void circle(Point pixel, int radius, Paint paint);

    void rect(Rect r, Paint paint);

    void bitmap(Bitmap b, Point pixel);
    void bitmap(Bitmap b, Point pixel, int color);

    void edge(TwoNodes nodes, Paint paint);
    void label(String text, Point pixel, Paint background, Paint frame);

    Paint createPaint();
}
