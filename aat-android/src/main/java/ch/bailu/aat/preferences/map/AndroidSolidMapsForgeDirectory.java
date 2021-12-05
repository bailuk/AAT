package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;
import ch.bailu.foc_android.FocAndroidFactory;

public class AndroidSolidMapsForgeDirectory extends SolidMapsForgeDirectory {

    private final Context context;

    public AndroidSolidMapsForgeDirectory(Context context) {
        super(new Storage(context), new FocAndroidFactory(context));
        this.context = context;
    }

    public String getDefaultValue() {
        ArrayList<String> list = new ArrayList<>(5);

        list = buildSelection(list);

        File external = context.getExternalFilesDir(null);

        if (external != null) {
            add_w(list, FocAndroid.factory(context, external.getAbsolutePath()));
        }

        if (list.size()>0) {
            return list.get(0);
        }
        return "";
    }

    public ArrayList<Foc> getWellKnownMapDirs() {
        final ArrayList<Foc> dirs = new ArrayList<>(5);
        final AndroidVolumes volumes = new AndroidVolumes(context);

        for (Foc f : volumes.getVolumes()) {
            add_dr(dirs, f.child(MAPS_DIR));
            add_dr(dirs, f.child(AppDirectory.DIR_AAT_DATA + "/" + MAPS_DIR));
            add_dr(dirs, f.child(ORUX_MAPS_DIR));
        }

        return dirs;
    }
}
