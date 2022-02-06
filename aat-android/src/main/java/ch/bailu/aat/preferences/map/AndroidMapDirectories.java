package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat_lib.preferences.SelectionList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.MapDirectories;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;
import ch.bailu.foc_android.FocAndroid;
import ch.bailu.foc_android.FocAndroidFactory;

public class AndroidMapDirectories implements MapDirectories {


    private Context context;

    public AndroidMapDirectories(Context context) {
        this.context = context;
    }

    @Override
    public ArrayList<Foc> getWellKnownMapDirs() {
        final ArrayList<Foc> dirs = new ArrayList<>(5);
        final AndroidVolumes volumes = new AndroidVolumes(context);

        for (Foc f : volumes.getVolumes()) {
            SelectionList.add_dr(dirs, f.child(SolidMapsForgeDirectory.MAPS_DIR));
            SelectionList.add_dr(dirs, f.child(AppDirectory.DIR_AAT_DATA + "/" + SolidMapsForgeDirectory.MAPS_DIR));
            SelectionList.add_dr(dirs, f.child(SolidMapsForgeDirectory.ORUX_MAPS_DIR));
        }

        return dirs;
    }

    @Override
    public Foc getDefault() {
        File external = context.getExternalFilesDir(null);
        return FocAndroid.factory(context, external.getAbsolutePath());
    }

    public static SolidMapsForgeDirectory createSolidMapsForgeDirectory(Context c) {
        final FocFactory foc = new FocAndroidFactory(c);
        final StorageInterface storage = new Storage(c);

        return new SolidMapsForgeDirectory(storage, foc, new AndroidMapDirectories(c));
    }

    public static SolidMapsForgeMapFile createSolidMapsForgeMapFile(Context c) {
        final FocFactory foc = new FocAndroidFactory(c);
        final StorageInterface storage = new Storage(c);

        return new SolidMapsForgeMapFile(storage, foc, new AndroidMapDirectories(c));
    }
}
