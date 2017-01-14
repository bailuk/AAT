package ch.bailu.aat.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat.util.ui.AppDensity;

public class NodePainter {
    private static final int STROKE_WIDTH=MapPaint.EDGE_WIDTH;
    private static final int RADIUS=5;

/*
    public static BitmapDrawable createNode(AppDensity res, Resources r) {
        return new BitmapDrawable(r, createNodeMF(res)..getAndroidBitmap());
    }
*/

    public static TileBitmap createNodeMF(AppDensity res) {


        final int color = Color.WHITE;


        int stroke_width=res.toDPi(STROKE_WIDTH);
        int radius = res.toDPi(RADIUS);
        int hsize = (radius+ stroke_width);
        int size = hsize * 2;

        SyncTileBitmap bmp = new SyncTileBitmap();

        bmp.set(size, true);

        Canvas canvas = bmp.getAndroidCanvas();
        Paint stroke = AndroidGraphicFactory.getPaint(MapPaint.createEdgePaint(res));
        stroke.setAntiAlias(true);

        Paint fill = new Paint();
        fill.setAntiAlias(false);
        fill.setStyle(Style.FILL);
        fill.setColor(color);

        canvas.drawCircle(hsize,hsize, radius, fill);
        canvas.drawCircle(hsize, hsize, radius, stroke);

        return bmp.getTileBitmap();
    }

}