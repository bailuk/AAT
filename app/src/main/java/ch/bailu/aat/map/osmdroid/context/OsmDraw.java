package ch.bailu.aat.map.osmdroid.context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.map.MapDraw;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapPaint;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.map.osmdroid.overlay.MapTwoNodes;
import ch.bailu.aat.map.osmdroid.overlay.NodePainter;
import ch.bailu.aat.util.graphic.AppTileBitmap;
import ch.bailu.aat.util.ui.AppDensity;

public class OsmDraw implements MapDraw {

    private final AppDensity res;
    public Canvas canvas;
    private final MapMetrics metrics;
    private AppTileBitmap nodeBitmap;

    private final org.mapsforge.core.graphics.Paint
            gridPaint,
            legendTextPaint,
            statusTextPaint,
            edgePaint,
            pointPaint,
            backgroundPaint;

    private final int textHeight;


    private final MapContext mcontext;

    public OsmDraw(MapMetrics m, MapContext c) {
        metrics = m;
        mcontext = c;
        res = c.getMetrics().getDensity();


        gridPaint= MapPaint.createGridPaint(res);
        statusTextPaint=MapPaint.createStatusTextPaint(res);
        legendTextPaint=MapPaint.createLegendTextPaint(res);
        legendTextPaint.setColor(Color.DKGRAY);



        edgePaint=MapPaint.createEdgePaint(res);
        pointPaint=MapPaint.createEdgePaint(res);
        pointPaint.setStyle(Style.FILL);



        backgroundPaint=MapPaint.createBackgroundPaint();

        nodeBitmap = NodePainter.createNodeMF(res);

        textHeight=statusTextPaint.getTextHeight("X");
    }


    public void init(Canvas c) {
        canvas=c;
    }



    @Override
    public void grid(Point center, int space) {
        for (int x = center.x; x< metrics.getRight(); x+=space)
            vLine(x);

        for (int x = center.x-space; x> metrics.getLeft(); x-=space)
            vLine(x);

        for (int y = center.y; y< metrics.getBottom(); y+=space)
            hLine(y);

        for (int y = center.y-space; y> metrics.getTop(); y-=space)
            hLine(y);
    }



    @Override
    public void hLine(int y) {
        canvas.drawLine(metrics.getLeft(), y, metrics.getRight(), y, convert(gridPaint));
    }

    @Override
    public void vLine(int x) {
        canvas.drawLine(x, metrics.getTop(), x, metrics.getBottom(), convert(gridPaint));

    }

    public void drawLine(Point a, Point b, Paint paint) {
        canvas.drawLine(a.x, a.y, b.x, b.y, paint);
    }


    @Override
    public void edge(TwoNodes nodes) {
        edge(nodes, edgePaint);
    }


    @Override
    public void edge(TwoNodes nodes, org.mapsforge.core.graphics.Paint paint) {
        drawLine(nodes.nodeA.pixel, nodes.nodeB.pixel, convert(edgePaint));
    }


    private void drawEdge(MapTwoNodes nodes, Paint paint) {
        drawLine(nodes.nodeA.pixel, nodes.nodeB.pixel, paint);
    }

    @Override
    public void rect(Rect rect, org.mapsforge.core.graphics.Paint paint) {
        canvas.drawRect(rect, AndroidGraphicFactory.getPaint(paint));
    }



    @Override
    public void circle(Point pixel, int radius, org.mapsforge.core.graphics.Paint paint) {
        canvas.drawCircle(pixel.x, pixel.y, radius, AndroidGraphicFactory.getPaint(paint));
    }


    @Override
    public void point(Point pixel) {
        canvas.drawCircle(pixel.x, pixel.y,
                res.toDPf(POINT_RADIUS),
                convert(pointPaint));
    }




    @Override
    public void textTop(String text, int line) {
        final int ts = (int) (statusTextPaint.getTextHeight(text)*line);

        canvas.drawText(text, metrics.getLeft()+MARGIN, metrics.getTop()+ts, convert(statusTextPaint));

    }



    @Override
    public void textBottom(String s, int line) {
        final int ts=(int)statusTextPaint.getTextHeight(s);
        canvas.drawText(s,
                metrics.getLeft()+MARGIN,
                metrics.getBottom()-MARGIN-(ts*line),
                convert(statusTextPaint));

    }


    @Override
    public void label(String text, Point pixel) {
        drawBackground(text, pixel);
        canvas.drawText(text, pixel.x+MARGIN, pixel.y, convert(legendTextPaint));
    }


    public void drawBackground(String text, Point pixel) {
        canvas.drawRect(pixel.x,
                pixel.y - MARGIN,
                pixel.x + textHeight + MARGIN*2,
                pixel.y + textHeight + MARGIN,
                convert(backgroundPaint));
    }

    public void drawSize(Drawable node, Point pixel, int size) {
        centerBounds(node, pixel, size);
        node.draw(canvas);
    }



    @Override
    public void bitmap(Bitmap node, Point pixel, int color) {
        bitmap(AndroidGraphicFactory.getBitmap(node), pixel, color);
    }



    private void bitmap(android.graphics.Bitmap b, Point pixel, int color) {
        Drawable node = new BitmapDrawable(mcontext.getContext().getResources(), b);

        centerBounds(node, pixel);
        node.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        node.draw(canvas);
    }

    private static void centerBounds(Drawable node, Point pixel, int size) {
        int hsize = size/2;

        node.setBounds(pixel.x-hsize, pixel.y-hsize,
                pixel.x+hsize, pixel.y+hsize);
    }




    private void draw(Drawable node, Point pixel) {
        centerBounds(node, pixel);
        node.draw(canvas);
    }

    private void draw(Drawable node, Point pixel, int c) {
        centerBounds(node, pixel);
        node.setColorFilter(c, PorterDuff.Mode.MULTIPLY);
        node.draw(canvas);
    }



    private static void centerBounds(Drawable node, Point pixel) {
        final int HSIZE = node.getIntrinsicWidth()/2;
        final int VSIZE = node.getIntrinsicHeight()/2;

        node.setBounds(pixel.x-HSIZE, pixel.y-VSIZE, pixel.x+HSIZE, pixel.y+VSIZE);
    }

    @Override
    public org.mapsforge.core.graphics.Paint getTextPaint() {
        return statusTextPaint;
    }

    @Override
    public org.mapsforge.core.graphics.Paint getGridPaint() {
        return gridPaint;
    }

    @Override
    public org.mapsforge.core.graphics.Paint getLegendPaint() {
        return legendTextPaint;
    }


    @Override
    public Bitmap getNodeBitmap() {
        return nodeBitmap.getTileBitmap();
    }


    private static Paint convert(org.mapsforge.core.graphics.Paint p) {
        return AndroidGraphicFactory.getPaint(p);
    }
}
