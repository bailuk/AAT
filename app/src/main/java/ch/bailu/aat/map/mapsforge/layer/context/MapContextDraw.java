package ch.bailu.aat.map.mapsforge.layer.context;


import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.graphic.AppTileBitmap;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.map.osm.overlay.NodePainter;

public class MapContextDraw {
    private final static int SPACE=5;

    private Canvas canvas=null;

    public final Paint textPaint = AndroidGraphicFactory.INSTANCE.createPaint();
    public final Paint gridPaint = AndroidGraphicFactory.INSTANCE.createPaint();
    public final Paint legendPaint = AndroidGraphicFactory.INSTANCE.createPaint();

    private final int textHeight;

    private int left=0, top=0, bottom=0;


    public AppTileBitmap nodeBitmap;

    public MapContextDraw(AppDensity res) {
        legendPaint.setColor(Color.BLUE);
        legendPaint.setTextSize(res.toDPi(15));


        gridPaint.setColor(Color.DKGRAY);


        textPaint.setTextSize(res.toDPi(20));
        textHeight = textPaint.getTextHeight("X")+5;

        nodeBitmap = NodePainter.createNodeMF(res);
    }

    public void init(MapContextMetrics metric) {
        left = metric.getLeft();
        top = metric.getTop();
        bottom = metric.getBottom();
    }

    public void init(Canvas c) {



        canvas = c;
    }


    public void grid(Point center, int space) {
        for (int x=(int)center.x; x < canvas.getWidth(); x+=space)
            vLine(x);

        for (int x=(int)center.x-space; x > 0; x-=space)
            vLine(x);

        for (int y=(int)center.y; y < canvas.getHeight(); y+=space)
            hLine(y);

        for (int y=(int)center.y-space; y > 0; y-=space)
            hLine(y);
    }


    public void vLine(int x) {
        canvas.drawLine(x, 0, x, canvas.getHeight(), textPaint);
    }

    public void hLine(int y) {
        canvas.drawLine(0, y, canvas.getWidth(), y, textPaint);
    }


    public void point(Point pixel) {
        canvas.drawCircle((int)pixel.x, (int)pixel.y, 10, textPaint);
    }


    public void textTop(String text, int line) {
        canvas.drawText(text, left + SPACE, top + SPACE + textHeight*line, textPaint);
    }

    public void textBottom(String s, int line) {
        canvas.drawText(s, left + SPACE, bottom - SPACE - textHeight*(line+1), textPaint);
    }

    public void circle(Point pixel, int radius, Paint paint) {
        canvas.drawCircle(pixel.x, pixel.y, radius, paint);
    }

    public void rect(Rect rect, Paint paint) {
        canvas.drawLine(rect.left, rect.top, rect.left, rect.bottom, paint);
        canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paint);
        canvas.drawLine(rect.right, rect.bottom, rect.right, rect.top, paint);
        canvas.drawLine(rect.right, rect.top, rect.left, rect.top, paint);
    }

    public void bitmap(Bitmap b, Point pixel, int color) {
        canvas.drawBitmap(b, pixel.x, pixel.y);
    }

    public void edge(MapContextTwoNodes nodes) {
        edge(nodes, textPaint);
    }

    public void edge(MapContextTwoNodes nodes, Paint paint) {
        canvas.drawLine(
                nodes.nodeA.pixel.x,
                nodes.nodeA.pixel.y,
                nodes.nodeB.pixel.x,
                nodes.nodeB.pixel.y,
                paint);
    }

    public void text(String text, Point pixel) {
        canvas.drawText(text, pixel.x, pixel.y, textPaint);
    }
}
