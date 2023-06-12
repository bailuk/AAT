package ch.bailu.aat.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.activities.CockpitActivity;
import ch.bailu.aat.activities.CockpitSplitActivity;
import ch.bailu.aat.activities.MapActivity;
import ch.bailu.aat.preferences.map.AndroidMapDirectories;
import ch.bailu.aat.util.AppPermission;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidWeight;
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack;
import ch.bailu.aat_lib.preferences.map.SolidPositionLock;
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme;
import ch.bailu.aat_lib.preferences.system.SolidStartCount;
import ch.bailu.foc_android.FocAndroidFactory;

public class PreferenceLoadDefaults {
    public PreferenceLoadDefaults(Activity context) {
        var storage = new Storage(context);
        var startCount = new SolidStartCount(storage);

        if (startCount.isFirstRun()) {
            setDefaults(context, storage);
            AppPermission.requestFromUser(context);
        }
        startCount.increment();
    }

    private void setDefaults(Context context, StorageInterface storage) {
        SolidRenderTheme stheme = new SolidRenderTheme(new AndroidMapDirectories(context).createSolidDirectory(), new FocAndroidFactory(context));
        new SolidMapTileStack(stheme).setDefaults();
        new SolidWeight(storage).setDefaults();
        new SolidPositionLock(storage, MapActivity.SOLID_KEY).setDefaults();
        new SolidPositionLock(storage, CockpitActivity.SOLID_KEY).setDefaults();
        new SolidPositionLock(storage, CockpitSplitActivity.SOLID_MAP_KEY).setDefaults();
    }
}
