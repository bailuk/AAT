package ch.bailu.aat.preferences;

import ch.bailu.aat.R;
import android.content.Context;


public class SolidTileSize extends SolidIndexList {
    private static String KEY="tile_size";

    public static final int DEFAULT_TILESIZE=256;
    private static final int STEP=32;

    private static final int[] VALUE_LIST = {
        DEFAULT_TILESIZE + STEP *0,
        DEFAULT_TILESIZE + STEP *1,
        DEFAULT_TILESIZE + STEP *2,
        DEFAULT_TILESIZE + STEP *3,
        DEFAULT_TILESIZE + STEP *4,
        DEFAULT_TILESIZE + STEP *5,
        DEFAULT_TILESIZE + STEP *6,
        DEFAULT_TILESIZE + STEP *7,
        DEFAULT_TILESIZE + STEP *8
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

    private String getString(int i) {
        return String.valueOf(VALUE_LIST[i]);
    }

    @Override
    public String getString() {
        return getString(getIndex());
    }


    @Override
    public String[] getStringArray() {
        String[] list = new String[length()];
        for (int i=0; i<length(); i++){
            list[i]=getString(i);
        }
        return list;
    }

}
