package ch.bailu.aat.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppDensity;

public class AndroidDraw implements MapDraw {
    private final static int SPACE=5;

    private Canvas canvas=null;

    private final Paint textPaint;
    private final Paint gridPaint;
    private final Paint legendPaint;

    private final int textHeight;

    private int left=0, top=0, bottom=0, right = 0;

    private final int point_radius;

    private final NodeBitmap nodePainter;
    private final Resources resources;


    public AndroidDraw(AppDensity res, Resources r) {
        legendPaint = MapPaint.createLegendTextPaint(res);
        gridPaint   = MapPaint.createGridPaint(res);
        textPaint   = MapPaint.createStatusTextPaint(res);
        textHeight  = textPaint.getTextHeight("X")+5;

        nodePainter = NodeBitmap.get(res);
        resources = r;

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
    public Drawable getNodeDrawable() {
        return nodePainter.getTileBitmap().getDrawable(resources);
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



    android.graphics.Paint bmpPaint = new android.graphics.Paint();

    @Override
    public void bitmap(Bitmap b, Point p, int c) {
        centerDrawable(d, p);

        bmpPaint.setColorFilter(PorterDuff.Mode.MULTIPLY);
        d.draw(AndroidGraphicFactory.getCanvas(canvas));
    }


    public static void centerDrawable(Drawable drawable, Point pixel) {
        final int HSIZE = drawable.getIntrinsicWidth()/2;
        final int VSIZE = drawable.getIntrinsicHeight()/2;

        drawable.setBounds(pixel.x-HSIZE, pixel.y-VSIZE, pixel.x+HSIZE, pixel.y+VSIZE);
    }




    @Override
    public void bitmap(Drawable d, Point p) {
        centerDrawable(d, p);
        d.draw(AndroidGraphicFactory.getCanvas(canvas));
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


    private static android.graphics.Canvas convert(Canvas c) {
        return AndroidGraphicFactory.getCanvas(c);
    }

    private static android.graphics.Paint convert(org.mapsforge.core.graphics.Paint p) {
        return AndroidGraphicFactory.getPaint(p);
    }

}
