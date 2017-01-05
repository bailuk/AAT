package ch.bailu.aat.util.graphic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class AppTileBitmap extends AppBitmap {

    public AppTileBitmap(int size) {
        this(size, false);
    }


    public AppTileBitmap(int size, boolean isTransparent) {
        super(AndroidGraphicFactory.INSTANCE.createTileBitmap(size, isTransparent));
    }

    @Override
    public TileBitmap getTileBitmap() {
        return (TileBitmap) this.getBitmap();
    }

}
