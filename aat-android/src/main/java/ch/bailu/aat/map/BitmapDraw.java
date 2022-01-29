package ch.bailu.aat.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import ch.bailu.aat_lib.util.Point;


public final class BitmapDraw {

    private final Paint paint = new Paint();
    private final Paint colorPaint = new Paint();


    public BitmapDraw() {
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }


    public void draw(Canvas c, Bitmap b, Point p) {
        c.drawBitmap(b, centerX(b,p), centerY(b,p), paint);
    }

    public void draw(Canvas c, Bitmap b, Rect r) {
        c.drawBitmap(b, null, r, paint);
    }


    public void draw(Canvas c, Bitmap b, Point p, int color) {
        colorPaint.setColorFilter(new LightingColorFilter(color, 0));
        c.drawBitmap(b, centerX(b,p), centerY(b,p), colorPaint);
    }


    private static int centerX(Bitmap b, Point p) {
        return center(b.getWidth(), p.x);
    }

    private static int centerY(Bitmap b, Point p) {
        return center(b.getHeight(), p.y);
    }

    private static int center(int size, int pos) {
        return pos - (size / 2);
    }
}
