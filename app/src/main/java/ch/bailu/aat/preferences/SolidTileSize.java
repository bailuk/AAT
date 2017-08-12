package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.services.cache.TileObject;


public class SolidTileSize extends SolidIndexList {
    private static final String KEY="tile_size";

    public static final int DEFAULT_TILESIZE= TileObject.TILE_SIZE;
    private static final int STEP=32;

    private final int tileSizeDP;


    private static final int[] VALUE_LIST = {
            DEFAULT_TILESIZE + STEP *8,
            DEFAULT_TILESIZE + STEP *7,
            DEFAULT_TILESIZE + STEP *6,
            DEFAULT_TILESIZE + STEP *5,
            DEFAULT_TILESIZE + STEP *4,
            DEFAULT_TILESIZE + STEP *3,
            DEFAULT_TILESIZE + STEP *2,
            DEFAULT_TILESIZE + STEP *1,
            DEFAULT_TILESIZE + STEP *0,
            DEFAULT_TILESIZE + STEP *8,
            DEFAULT_TILESIZE + STEP *16,
            DEFAULT_TILESIZE + STEP *32,
            DEFAULT_TILESIZE + STEP *64,

    };

    public SolidTileSize(Storage s) {
        super(s, KEY);
        tileSizeDP = new AppDensity(s.getContext()).toDPi(DEFAULT_TILESIZE);
    }


    public SolidTileSize(Context context) {
        this(Storage.global(context));
    }


    public int getTileSize() {
        final int i = getIndex();
        if (i==0) {
            return tileSizeDP;
        }
        return VALUE_LIST[getIndex()];
    }

    @Override
    public String getLabel() {
        return this.getContext().getString(R.string.p_tile_size);
    }

    @Override
    public int length() {
        return VALUE_LIST.length;
    }

    @Override
    public String getValueAsString(int i) {
        if (i==0)  return tileSizeDP + " " + getContext().getString(R.string.auto);

        return String.valueOf(VALUE_LIST[i]);
    }
}
