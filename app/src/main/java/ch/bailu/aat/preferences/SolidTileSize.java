package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;


public class SolidTileSize extends SolidIndexList {
    private static final String KEY="tile_size";

    public static final int DEFAULT_TILESIZE=256;
    private static final int STEP=32;

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
            DEFAULT_TILESIZE + STEP *16,
            DEFAULT_TILESIZE + STEP *32,
            DEFAULT_TILESIZE + STEP *64,

    };

    public SolidTileSize(Storage s) {
        super(s, KEY);
    }


    public SolidTileSize(Context context) {
        this(Storage.global(context));
    }


    public int getTileSize() {
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
        return String.valueOf(VALUE_LIST[i]);
    }

}
