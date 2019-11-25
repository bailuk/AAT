package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.preferences.SolidCheckList;
import ch.bailu.aat.preferences.Storage;


public final class SolidMapTileStack extends SolidCheckList {


    private final static String KEY = "tile_overlay_";



    public final static Source[] SOURCES = new Source[] {
            MapsForgeSource.MAPSFORGE,
            DownloadSource.MAPNIK,
            DownloadSource.OPEN_TOPO_MAP,
            DownloadSource.OPEN_CYCLE_MAP,
            Source.ELEVATION_COLOR,
            Source.ELEVATION_HILLSHADE,
            DownloadSource.TRANSPORT_OVERLAY,
            DownloadSource.TRAIL_SKATING,
            DownloadSource.TRAIL_HIKING,
            DownloadSource.TRAIL_MTB,
            DownloadSource.TRAIL_CYCLING,
    };




    private final SolidBoolean[] enabledArray = new SolidBoolean[SOURCES.length];


    public SolidMapTileStack (Context context) {
        this (context, 0);
    }

    // FIXME: use preset for tile stack
    private SolidMapTileStack (Context context, int preset) {

        for (int i=0; i<enabledArray.length; i++) {
            enabledArray[i]=new SolidBoolean(context, KEY+preset+"_"+i);
        }
    }


    @Override
    public CharSequence[] getStringArray() {
        String mapsForgeLabel =
                MapsForgeSource.NAME
                        + " " + new SolidRenderTheme(getContext()).getValueAsThemeName();
        String[] array = new String[SOURCES.length];
        array[0] = mapsForgeLabel;
        for (int i=1; i<SOURCES.length; i++)
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


    public void setDefaults() {
        for (int i=0; i < SOURCES.length; i++) {
            if (SOURCES[i] == DownloadSource.MAPNIK) {
                setEnabled(i, true);
                break;
            }
        }
    }
}
