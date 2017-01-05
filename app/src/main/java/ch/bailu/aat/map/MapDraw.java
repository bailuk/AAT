package ch.bailu.aat.map;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Paint;

public interface MapDraw {
    final static int POINT_RADIUS=10;
    final static int MARGIN=5;

    Paint getTextPaint();
    Paint getGridPaint();
    Paint getLegendPaint();

    Bitmap getNodeBitmap();


    void grid(Point center, int space);

    void vLine(int x);
    void hLine(int y);

    void point(Point pixel);
    void textTop(String text, int line);

    void textBottom(String s, int line);
    void circle(Point pixel, int radius, Paint paint);

    void rect(Rect rect, Paint paint);

    void bitmap(Drawable drawable, Point pixel);
    void bitmap(Bitmap b, Point pixel, int color);

    public void edge(TwoNodes nodes);
    public void edge(TwoNodes nodes, Paint paint);

    public void label(String text, Point pixel);
}
