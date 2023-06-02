package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;


public class SolidTileSize extends SolidIndexList {
    private static final String KEY = "tile_size";

    public static final int DEFAULT_TILESIZE = 256;
    public static final int DEFAULT_TILESIZE_BYTES = DEFAULT_TILESIZE * DEFAULT_TILESIZE * 4;

    private static final int STEP = 32;

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

    public SolidTileSize(StorageInterface storage, AppDensity density) {
        super(storage, KEY);
        tileSizeDP = density.toPixel_i(DEFAULT_TILESIZE);
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
        return Res.str().p_tile_size();
    }

    @Override
    public int length() {
        return VALUE_LIST.length;
    }

    @Override
    public String getValueAsString(int index) {
        int i = validate(index);
        if (i == 0)  return toDefaultString(String.valueOf(tileSizeDP));
        return String.valueOf(VALUE_LIST[i]);
    }
}
