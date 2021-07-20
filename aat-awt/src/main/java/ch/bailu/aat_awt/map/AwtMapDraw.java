package ch.bailu.aat_awt.map;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;

import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.map.MapDraw;
import ch.bailu.aat_lib.map.MapMetrics;
import ch.bailu.aat_lib.map.MapPaint;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.Rect;
import ch.bailu.aat_lib.map.TwoNodes;

public class AwtMapDraw implements MapDraw {

    private final static int SPACE=5;

    private Canvas canvas=null;

    private final Paint textPaint;
    private final Paint gridPaint;
    private final Paint legendPaint;


    private final int textHeight;

    private int left=0, top=0, bottom=0, right = 0;

    private final int point_radius;

    public AwtMapDraw() {
        AppDensity res = new AppDensity();

        legendPaint = MapPaint.createLegendTextPaint(res);
        gridPaint   = MapPaint.createGridPaint(res);
        textPaint   = MapPaint.createStatusTextPaint(res);

        textHeight  = textPaint.getTextHeight("X")+5;

        point_radius = res.toPixel_i(POINT_RADIUS);
    }



    public void init(Canvas c, MapMetrics metric) {
        canvas = c;
        init(metric);
    }

    private void init(MapMetrics metric) {
        left   = metric.getLeft();
        top    = metric.getTop();
        bottom = metric.getBottom();
        right  = metric.getRight();
    }


    @Override
    public Paint getGridPaint() {
        return gridPaint;
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
