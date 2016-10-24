package ch.bailu.aat.views.map.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import ch.bailu.aat.helpers.AppDensity;

public class MapCanvas {
    public static final int EDGE_WIDTH=2;

    private final static int POINT_RADIUS=3;
    private final static int MARGIN=5;
    private final static float TEXT_SIZE=20;
    private final static int ALPHA=150;
    private final static int BG_ALPHA=125;

    private final AppDensity res;

    private final Paint
            gridPaint,
            legendTextPaint,
            statusTextPaint,
            edgePaint,
            pointPaint,
            backgroundPaint;

    private final FontMetrics legendMetrics;

    public Canvas canvas;
    private final MapProjection projection;
    


    public MapCanvas(Context context, MapProjection p, AppDensity r) {
        res = r;
        projection=p;

        gridPaint=createGridPaint();
        statusTextPaint=createTextPaint(context, TEXT_SIZE);
        legendTextPaint=createTextPaint(context, TEXT_SIZE/3*2);
        legendTextPaint.setColor(Color.DKGRAY);
        legendMetrics = legendTextPaint.getFontMetrics();
                        
        edgePaint=createEdgePaint();
        pointPaint=createEdgePaint();
        pointPaint.setStyle(Style.FILL);


        
        backgroundPaint=createBackgroundPaint();
    }

    public void init(Canvas c) {
        canvas=c;
    }

    public static Paint createBackgroundPaint() {
        Paint p=new Paint();
        p.setColor(Color.WHITE);
        p.setAlpha(BG_ALPHA);
        p.setStyle(Style.FILL);
        p.setAntiAlias(false);
        return p;
    }
    
    public Paint  createGridPaint() {
        Paint p=new Paint();
        p.setColor(Color.DKGRAY);
        p.setAlpha(ALPHA);
        p.setStyle(Style.STROKE);
        p.setAntiAlias(false);
        p.setDither(false);
        p.setStrokeWidth(Math.max(1, res.toDPf(1)));
        return p;
    }

    public Paint  createTextPaint(Context context, float size) {
        Paint p=new Paint();
        p.setColor(Color.BLACK);

        p.setTextSize(res.toSDPf(size));
        p.setFakeBoldText(true);
        p.setStyle(Style.FILL);
        p.setAntiAlias(true);
        return p;
    }


    public static Paint createEdgePaint() {
        Paint edge = new Paint();

        edge.setStrokeWidth(EDGE_WIDTH);

        edge.setAntiAlias(false);
        edge.setColor(Color.DKGRAY);
        edge.setStyle(Style.STROKE);
        return edge;
    }

    public void drawGrid(Point center, int space) {
        for (int x=center.x; x<projection.screen.right; x+=space) 
            drawVLine(x);

        for (int x=center.x-space; x>projection.screen.left; x-=space)
            drawVLine(x);

        for (int y=center.y; y<projection.screen.bottom; y+=space)
            drawHLine(y);

        for (int y=center.y-space; y>projection.screen.top; y-=space) 
            drawHLine(y);
    }


    // FIXME: drawLine does not work correctly in zoomlevel 17
    public void drawHLine(int y) {
        canvas.drawLine(projection.screen.left, y, projection.screen.right, y, gridPaint);
    }


    public void drawVLine(int x) {
        canvas.drawLine(x, projection.screen.top, x, projection.screen.bottom, gridPaint);

    }

    public void drawLine(Point a, Point b, Paint paint) {
        canvas.drawLine(a.x, a.y, b.x, b.y, paint);
    }


    public void drawEdge(MapTwoNodes nodes) {
        drawLine(nodes.nodeA.pixel, nodes.nodeB.pixel, edgePaint);

    }


    public void drawEdge(MapTwoNodes nodes, Paint paint) {
        drawLine(nodes.nodeA.pixel, nodes.nodeB.pixel, paint);
    }


    public void drawRect(Rect rect) {
        canvas.drawRect(rect, gridPaint);

    }


    public void drawCircle(Point pixel, int radius, Paint paint) {
        canvas.drawCircle(pixel.x, pixel.y, radius, paint);
    }


    public void drawPoint(Point pixel) {
        canvas.drawCircle(pixel.x, pixel.y, 
                res.toDPf(POINT_RADIUS),
                pointPaint);
    }




    public void drawTextTop(String text, int i) {
        final int ts = (int) (statusTextPaint.getTextSize()*i);

        canvas.drawText(text, projection.screen.left+MARGIN, projection.screen.top+ts, statusTextPaint);

    }


    public void drawTextBottom(String text, int line) {
        final int ts=(int)statusTextPaint.getTextSize();
        canvas.drawText(text, 
                projection.screen.left+MARGIN, 
                projection.screen.bottom-MARGIN-(ts*line), 
                statusTextPaint);
        
    }
    
    

    public void drawText(String text, Point pixel) {
        drawBackground(text, pixel);
        canvas.drawText(text, pixel.x+MARGIN, pixel.y, legendTextPaint);
    }
    
    public void drawBackground(String text, Point pixel) {
        canvas.drawRect(pixel.x,
                pixel.y + legendMetrics.top - MARGIN,
                pixel.x + legendTextPaint.measureText(text) + MARGIN*2, 
                pixel.y + legendMetrics.bottom + MARGIN, 
                backgroundPaint);
    }

    public void drawSize(Drawable node, Point pixel, int size) {
        centerBounds(node, pixel, size);
        node.draw(canvas);
    }



    public void drawSize(BitmapDrawable node, Point pixel, int size, int c) {
        centerBounds(node, pixel, size);
        node.setColorFilter(c, PorterDuff.Mode.MULTIPLY);
        node.draw(canvas);
    }


    private static void centerBounds(Drawable node, Point pixel, int size) {
        int hsize = size/2;

        node.setBounds(pixel.x-hsize, pixel.y-hsize,
                pixel.x+hsize, pixel.y+hsize);
    }


    public void draw(Drawable node, Point pixel) {
        centerBounds(node, pixel);
        node.draw(canvas);
    }
    
    public void draw(Drawable node, Point pixel, int c) {
        centerBounds(node, pixel);
        node.setColorFilter(c, PorterDuff.Mode.MULTIPLY);
        node.draw(canvas);
    }


    
    private static void centerBounds(Drawable node, Point pixel) {
        final int HSIZE = node.getIntrinsicWidth()/2;
        final int VSIZE = node.getIntrinsicHeight()/2;
        
        node.setBounds(pixel.x-HSIZE, pixel.y-VSIZE, pixel.x+HSIZE, pixel.y+VSIZE);
    }

}
