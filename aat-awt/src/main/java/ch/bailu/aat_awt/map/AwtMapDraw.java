package ch.bailu.aat_awt.map;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;

import ch.bailu.aat_lib.map.MapDraw;
import ch.bailu.aat_lib.map.MapsForgeMetrics;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.Rect;
import ch.bailu.aat_lib.map.TwoNodes;

public class AwtMapDraw implements MapDraw {

    public void init(Canvas canvas, MapsForgeMetrics metrics) {
    }


    @Override
    public Paint getGridPaint() {
        return null;
    }

    @Override
    public Bitmap getNodeBitmap() {
        return null;
    }

    @Override
    public void grid(Point center, int space) {

    }

    @Override
    public void vLine(int x) {

    }

    @Override
    public void hLine(int y) {

    }

    @Override
    public void point(Point pixel) {

    }

    @Override
    public void textTop(String text, int line) {

    }

    @Override
    public void textBottom(String s, int line) {

    }

    @Override
    public void circle(Point pixel, int radius, Paint paint) {

    }

    @Override
    public void rect(Rect r, Paint paint) {

    }

    @Override
    public void bitmap(Bitmap b, Point pixel) {

    }

    @Override
    public void bitmap(Bitmap b, Point pixel, int color) {

    }

    @Override
    public void edge(TwoNodes nodes, Paint paint) {

    }

    @Override
    public void label(String text, Point pixel, Paint background, Paint frame) {

    }

    @Override
    public Paint createPaint() {
        return AwtGraphicFactory.INSTANCE.createPaint();
    }


}
