package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;


public class SolidMapTileStack extends SolidCheckList {


    private final static String KEY = "tile_overlay_";



    public final static Source[] SOURCES = new Source[] {
            MapsForgeSource.MAPSFORGE,
            DownloadSource.MAPNIK,
            Source.ELEVATION_COLOR,
            Source.ELEVATION_HILLSHADE,
            DownloadSource.TRANSPORT_OVERLAY,
            DownloadSource.TRAIL_SKATING,
            DownloadSource.TRAIL_HIKING,
            DownloadSource.TRAIL_MTB,
            DownloadSource.TRAIL_CYCLING,
    };




    private final SolidBoolean[] enabledArray = new SolidBoolean[SOURCES.length];


    public SolidMapTileStack (Context context, int preset) {
        Storage s = Storage.global(context);

        for (int i=0; i<enabledArray.length; i++) {
            enabledArray[i]=new SolidBoolean(s, KEY+preset+"_"+i);
        }
    }


    @Override
    public CharSequence[] getStringArray() {
        String[] array = new String[SOURCES.length];
        for (int i=0; i<SOURCES.length; i++)
            array[i] = SOURCES[i].getName();
        return array;
    }

    @Override
    public boolean[] getEnabledArray() {
        boolean[] array = new boolean[enabledArray.length];
        for (int i=0; i<enabledArray.length; i++)
            array[i] = enabledArray[i].isEnabled();
        return array;
    }


    @Override
    public void setEnabled(int i, boolean isChecked) {
        i=Math.min(enabledArray.length, i);
        i=Math.max(0, i);
        enabledArray[i].setValue(isChecked);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_map);
    }


    @Override
    public String getKey() {
        return KEY;
    }



    @Override
    public Storage getStorage() {
        return enabledArray[0].getStorage();
    }


    @Override
    public boolean hasKey(String s) {
        for (SolidBoolean anEnabledArray : enabledArray) {
            if (anEnabledArray.hasKey(s)) {
                return true;
            }
        }
        return false;
    }


}
