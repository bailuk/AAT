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
        for (int x = center.x; x < right; x+=space)
            vLine(x);

        for (int x =  (center.x-space); x > left; x-=space)
            vLine(x);

        for (int y = center.y; y < bottom; y+=space)
            hLine(y);

        for (int y = (center.y-space); y > top; y-=space)
            hLine(y);
    }



    @Override
    public void vLine(int x) {
        canvas.drawLine(x, top, x, bottom, gridPaint);
    }

    @Override
    public void hLine(int y) {
        canvas.drawLine(left, y, right, y, gridPaint);
    }


    @Override
    public void point(Point pixel) {
        circle(pixel, point_radius, gridPaint);
    }

    @Override
    public void textTop(String text, int line) {
        canvas.drawText(text, left + SPACE, top + SPACE + textHeight*line, textPaint);
    }

    @Override
    public void textBottom(String s, int line) {
        canvas.drawText(s, left + SPACE, bottom - SPACE - textHeight*(line+1), textPaint);
    }


    @Override
    public void circle(Point pixel, int radius, Paint paint) {
        canvas.drawCircle((int)pixel.x, (int)pixel.y, radius, paint);
    }

    @Override
    public void rect(Rect rect, Paint paint) {
        canvas.drawLine(rect.left,  rect.top,    rect.left,  rect.bottom, paint);
        canvas.drawLine(rect.left,  rect.bottom, rect.right, rect.bottom, paint);
        canvas.drawLine(rect.right, rect.bottom, rect.right, rect.top, paint);
        canvas.drawLine(rect.right, rect.top,    rect.left,  rect.top, paint);
    }


    @Override
    public void bitmap(Bitmap b, Point pixel) {

    }

    @Override
    public void bitmap(Bitmap b, Point pixel, int color) {

    }


    @Override
    public void edge(TwoNodes nodes, Paint paint) {
        canvas.drawLine(
                nodes.nodeA.pixel.x,
                nodes.nodeA.pixel.y,
                nodes.nodeB.pixel.x,
                nodes.nodeB.pixel.y,
                paint);
    }

    @Override
    public void label(String text, Point pixel, Paint background, Paint frame) {
        drawBackground(text, pixel, background);
        drawBackground(text, pixel, frame);
        canvas.drawText(text, (int)pixel.x, (int)pixel.y, legendPaint);
    }

    public void drawBackground(String text, Point pixel, Paint paint) {
    }

    @Override
    public Paint createPaint() {
        return AwtGraphicFactory.INSTANCE.createPaint();
    }


}
