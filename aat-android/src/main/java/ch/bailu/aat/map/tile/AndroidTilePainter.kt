package ch.bailu.aat.map.tile;

import android.graphics.Bitmap;
import android.graphics.Paint;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import javax.annotation.Nonnull;

import ch.bailu.aat.map.AndroidDraw;
import ch.bailu.aat_lib.map.TilePainter;
import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat_lib.util.Rect;

public class AndroidTilePainter implements TilePainter {
    private android.graphics.Rect rectCache = new android.graphics.Rect();

    @Override
    public void paint(@Nonnull TileBitmap tileBitmap, @Nonnull Canvas canvas, @Nonnull Rect rect, @Nonnull org.mapsforge.core.graphics.Paint paint) {
        final Bitmap bitmap = AndroidGraphicFactory.getBitmap(tileBitmap);
        if (bitmap != null) {
            AndroidDraw.convert(canvas).drawBitmap(bitmap, null, AndroidDraw.convert(rect, rectCache), AndroidDraw.convert(paint));
        }
    }

    @Override
    public org.mapsforge.core.graphics.Paint createPaint(@Nonnull Source source) {
        org.mapsforge.core.graphics.Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        Paint androidPaint = AndroidDraw.convert(paint);

        androidPaint.setAlpha(source.getAlpha());

        if (source.filterBitmap()) {
            androidPaint.setFlags(Paint.FILTER_BITMAP_FLAG);
        }
        return paint;
    }
}
