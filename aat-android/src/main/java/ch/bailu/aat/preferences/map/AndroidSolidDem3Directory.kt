package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat_lib.preferences.SelectionList;
import ch.bailu.aat_lib.preferences.map.SolidDem3Directory;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroidFactory;

public class AndroidSolidDem3Directory extends SolidDem3Directory {
    private final Context context;

    public AndroidSolidDem3Directory(Context c) {
        super(new Storage(c), new FocAndroidFactory(c));
        context = c;
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        AndroidVolumes volumes = new AndroidVolumes(context);

        // exists and is writeable
        for (Foc vol : volumes.getVolumes()) {
            final Foc dem3 =  vol.child(DEM3_DIR);
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR);

            SelectionList.add_w(list, aat);
            SelectionList.add_w(list, dem3);
        }


        // exists not but parent is writeable
        for (Foc vol : volumes.getVolumes()) {
            final Foc aat_dem3 = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR);
            final Foc dem3 =  vol.child(DEM3_DIR);


            if (!aat_dem3.exists())  SelectionList.add_w(list, aat_dem3.parent(), aat_dem3);
            if (!dem3.exists()) SelectionList.add_w(list, dem3.parent(), dem3);
        }

        // exists and is read only
        for (Foc vol : volumes.getVolumes()) {
            final Foc aat_dem3 = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR);
            final Foc dem3 =  vol.child(DEM3_DIR);

            SelectionList.add_ro(list, aat_dem3);
            SelectionList.add_ro(list, dem3);
        }

        // official writeable cache directory
        for (Foc cache : volumes.getCaches()) {
            final Foc dem3 = cache.child(DEM3_DIR);
            SelectionList.add_w(list, cache, dem3);
        }


        return list;
    }
}
