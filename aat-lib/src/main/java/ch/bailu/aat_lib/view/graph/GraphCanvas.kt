package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.util.Point;

public interface GraphCanvas {
    void drawLine(int x1, int y1, int x2, int y2);
    void drawLine(Point pa, Point pb, int color);
    void drawText(String text, int x, int y);
    void drawBitmap(Point pa, int color);
    int getTextSize();
}
