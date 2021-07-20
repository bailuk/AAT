package ch.bailu.aat.map;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.SparseArray;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.map.MapPaint;

public final class NodeBitmap {

    private static final int STROKE_WIDTH= MapPaint.EDGE_WIDTH_LINE;
    private static final int RADIUS=6;

    private static final SparseArray<NodeBitmap> nodes = new SparseArray<>(10);

    public static NodeBitmap get(AppDensity res) {
        int radius = res.toPixel_i(RADIUS);

        NodeBitmap node = nodes.get(radius);

        if (node == null) {
            node = new NodeBitmap(radius, res);
            nodes.append(radius, node);
        }

        return node;
    }

    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private NodeBitmap(int radius, AppDensity res) {

        final int color = Color.WHITE;

        int stroke_width = res.toPixel_i(STROKE_WIDTH);
        int hsize = (radius+ stroke_width);
        int size = hsize * 2;



        bitmap.set(size, true);

        Canvas canvas = bitmap.getAndroidCanvas();
        Paint stroke = AndroidGraphicFactory.getPaint(MapPaint.createEdgePaintLine(res));
        stroke.setAntiAlias(true);

        Paint fill = new Paint();
        fill.setAntiAlias(false);
        fill.setStyle(Style.FILL);
        fill.setColor(color);
        fill.setAlpha(150);

        canvas.drawCircle(hsize,hsize, radius, fill);
        canvas.drawCircle(hsize, hsize, radius, stroke);
    }


    public SyncTileBitmap getTileBitmap() {
        return bitmap;
    }
}
