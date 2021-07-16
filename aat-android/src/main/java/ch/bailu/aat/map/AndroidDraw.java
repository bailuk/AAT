package ch.bailu.aat.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppDensity;

public final class AndroidDraw implements MapDraw {
    private final static int SPACE=5;

    private Canvas canvas=null;

    private final Paint textPaint;
    private final Paint gridPaint;
    private final Paint legendPaint;


    private final BitmapDraw bitmapCanvas = new BitmapDraw();

    private final int textHeight;

    private int left=0, top=0, bottom=0, right = 0;

    private final int point_radius;

    private final NodeBitmap nodePainter;

    public AndroidDraw(AppDensity res) {
        legendPaint = MapPaint.createLegendTextPaint(res);
        gridPaint   = MapPaint.createGridPaint(res);
        textPaint   = MapPaint.createStatusTextPaint(res);

        textHeight  = textPaint.getTextHeight("X")+5;

        nodePainter = NodeBitmap.get(res);

        point_radius = res.toPixel_i(POINT_RADIUS);

    }

    private void init(MapMetrics metric) {
        left   = metric.getLeft();
        top    = metric.getTop();
        bottom = metric.getBottom();
        right  = metric.getRight();
    }

    public void init(Canvas c, MapMetrics metric) {
        canvas = c;
        init(metric);
    }

    public void init(android.graphics.Canvas c, MapMetrics metric) {
        init(AndroidGraphicFactory.createGraphicContext(c),metric);
    }


    public Canvas getCanvas() {
        return canvas;
    }


    @Override
    public Paint getGridPaint() {
        return gridPaint;
    }

    @Override
    public Bitmap getNodeBitmap() {
        return nodePainter.getTileBitmap().getAndroidBitmap();
    }

    @Override
    public void grid(Point center, int space) {
        for (int x=center.x; x < right; x+=space)
            vLine(x);

        for (int x=center.x-space; x > left; x-=space)
            vLine(x);

        for (int y=center.y; y < bottom; y+=space)
            hLine(y);

        for (int y=center.y-space; y > top; y-=space)
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
        canvas.drawCircle(pixel.x, pixel.y, radius, paint);
    }


    @Override
    public void rect(Rect rect, Paint paint) {
        canvas.drawLine(rect.left,  rect.top,    rect.left,  rect.bottom, paint);
        canvas.drawLine(rect.left,  rect.bottom, rect.right, rect.bottom, paint);
        canvas.drawLine(rect.right, rect.bottom, rect.right, rect.top, paint);
        canvas.drawLine(rect.right, rect.top,    rect.left,  rect.top, paint);
    }



    @Override
    public void bitmap(Bitmap b, Point p, int c) {
        bitmapCanvas.draw(convert(canvas), b, p, c);
    }



    private static Point center(Bitmap b, Point p) {
        p.x = p.x - (b.getWidth() / 2);
        p.y = p.y - (b.getHeight() / 2);
        return p;
    }


    @Override
    public void bitmap(Bitmap b, Point p) {
        bitmapCanvas.draw(convert(canvas), b, p);
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
        canvas.drawText(text, pixel.x, pixel.y, legendPaint);
    }


    public void drawBackground(String text, Point pixel, Paint paint) {
        android.graphics.Paint lp = convert(legendPaint);

        android.graphics.Paint.FontMetrics legendMetrics = lp.getFontMetrics();

        convert(canvas).drawRect(pixel.x,
                pixel.y + legendMetrics.top - MARGIN,
                pixel.x + lp.measureText(text) + MARGIN*2,
                pixel.y + legendMetrics.bottom + MARGIN,
                convert(paint));
    }

    public static android.graphics.Canvas convert(org.mapsforge.core.graphics.Canvas c) {
        return AndroidGraphicFactory.getCanvas(c);
    }

    public static android.graphics.Paint convert(org.mapsforge.core.graphics.Paint p) {
        return AndroidGraphicFactory.getPaint(p);
    }


}
