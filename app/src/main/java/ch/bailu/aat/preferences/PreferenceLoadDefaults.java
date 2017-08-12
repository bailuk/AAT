package ch.bailu.aat.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.util.AppPermission;

public class PreferenceLoadDefaults {
    private static final String KEY_STARTCOUNT="start_count";


    public PreferenceLoadDefaults (Activity context) {
        SolidLong startCount = new SolidLong(Storage.global(context), KEY_STARTCOUNT);

        if (startCount.getValue() == 0) {
            setDefaults(context);
            AppPermission.requestFromUser(context);
        }
        startCount.setValue(startCount.getValue()+1);
    }


    private void setDefaults(Context context) {
        setDefaultMap(context);
        new SolidWeight(context).setValue(75);

        for (int i=0; i<new SolidPreset(context).length(); i++) {
            new OldSolidMET(context,i).setIndex(i);
        }
    }


    public static long getStartCount(Context context) {
        return new SolidLong(Storage.global(context), KEY_STARTCOUNT).getValue();
    }


    public void setDefaultMap(Context context) {
        for (int i=0; i < SolidMapTileStack.SOURCES.length; i++) {
            if (SolidMapTileStack.SOURCES[i] == DownloadSource.MAPNIK) {
                new SolidMapTileStack(context).setEnabled(i, true);
                break;
            }
        }
    }
}
