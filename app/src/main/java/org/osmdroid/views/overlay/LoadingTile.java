package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import ch.bailu.aat.helpers.AppLog;

public class LoadingTile {
    private static Drawable tile;
    private static Bitmap bitmap;


    private final static int BACKGROUND_COLOR = Color.rgb(216, 208, 208);
    private final static int LINE_COLOR = Color.rgb(200, 192, 192);
    private final static int SIZE = 256;

    public synchronized static Bitmap getBitmap() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(SIZE, SIZE,
                    Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            final Paint paint = new Paint();
            canvas.drawColor(BACKGROUND_COLOR);
            paint.setColor(LINE_COLOR);
            paint.setStrokeWidth(0);
            final int lineSize = SIZE / 16;
            for (int a = 0; a < SIZE; a += lineSize) {
                canvas.drawLine(0, a, SIZE, a, paint);
                canvas.drawLine(a, 0, a, SIZE, paint);
            }
        }
        return bitmap;
    }


    public synchronized static Drawable getDrawable(Context context) {
        if (tile == null) {
            try {
                bitmap = getBitmap();
                tile = new BitmapDrawable(context.getResources(), bitmap);
            } catch (final OutOfMemoryError e) {
                AppLog.e(context, e);
                System.gc();
            }
        }
        return tile;
    }
}
