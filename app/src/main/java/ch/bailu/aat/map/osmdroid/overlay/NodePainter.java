package ch.bailu.aat.map.osmdroid.overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;

import ch.bailu.aat.util.graphic.AppTileBitmap;
import ch.bailu.aat.util.ui.AppDensity;

public class NodePainter {
    private static final int STROKE_WIDTH=MapCanvas.EDGE_WIDTH;
    private static final int RADIUS=5;


    public static BitmapDrawable createNode(AppDensity res, Resources r) {
        return new BitmapDrawable(r, createNodeMF(res).getBitmap());
    }


    public static AppTileBitmap createNodeMF(AppDensity res) {
        final int color = Color.WHITE;


        int stroke_width=res.toDPi(STROKE_WIDTH);
        int radius = res.toDPi(RADIUS);
        int hsize = (radius+ stroke_width);
        int size = hsize * 2;

        AppTileBitmap bmp = new AppTileBitmap(size, true);


        Bitmap bitmap = bmp.getBitmap();
        Canvas canvas = new Canvas(bitmap);
        Paint stroke = MapCanvas.createEdgePaint(res);
        stroke.setAntiAlias(true);

        Paint fill = new Paint();
        fill.setAntiAlias(false);
        fill.setStyle(Style.FILL);
        fill.setColor(color);

        canvas.drawCircle(hsize,hsize, radius, fill);
        canvas.drawCircle(hsize, hsize, radius, stroke);


        return bmp;
    }

}
