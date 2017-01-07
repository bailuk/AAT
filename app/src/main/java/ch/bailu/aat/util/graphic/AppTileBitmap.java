package ch.bailu.aat.util.graphic;

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
