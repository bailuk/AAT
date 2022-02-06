package ch.bailu.aat.preferences.map;

import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.SolidCheckList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme;
import ch.bailu.aat_lib.resources.Res;


public final class SolidMapTileStack extends SolidCheckList {


    private final static String KEY = "tile_overlay_";



    public final static int FIRST_OVERLAY_INDEX = 4;

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

    private final SolidRenderTheme srenderTheme;

    public SolidMapTileStack (SolidRenderTheme srender) {
        this (srender, 0);

    }

    // FIXME: use preset for tile stack
    private SolidMapTileStack (SolidRenderTheme srender, int preset) {

        for (int i=0; i<enabledArray.length; i++) {
            enabledArray[i]=new SolidBoolean(srender.getStorage(), KEY+preset+"_"+i);
        }
        srenderTheme = srender;
    }


    @Override
    public CharSequence[] getStringArray() {
        String mapsForgeLabel =
                MapsForgeSource.NAME
                        + " " + srenderTheme.getValueAsThemeName();
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
        return Res.str().p_map();
    }


    @Override
    public String getKey() {
        return KEY;
    }



    @Override
    public StorageInterface getStorage() {
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
