package ch.bailu.aat.views.map.overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;

public class NodePainter {
    private static final int STROKE_WIDTH=MapCanvas.EDGE_WIDTH;
    private static final int RADIUS=7;
    private static final int HSIZE = (RADIUS+STROKE_WIDTH);
    private static final int SIZE = HSIZE*2;

    public static BitmapDrawable createNode(Resources res) {
        final int color = Color.WHITE;
        Bitmap bitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint stroke = MapCanvas.createEdgePaint();
        stroke.setAntiAlias(true);

        Paint fill = new Paint();
        fill.setAntiAlias(false);
        fill.setStyle(Style.FILL);
        fill.setColor(color);
        
        canvas.drawCircle(HSIZE, HSIZE, RADIUS, fill);
        canvas.drawCircle(HSIZE, HSIZE, RADIUS, stroke);

        
        return new BitmapDrawable(res, bitmap);
    }
}
