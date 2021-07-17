package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public class AndroidSolidTileCacheDirectory extends SolidTileCacheDirectory {

    private final Context context;

    public AndroidSolidTileCacheDirectory(Context c) {
        super(new Storage(c), new AndroidFocFactory(c));
        context = c;
    }






    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        AndroidVolumes volumes = new AndroidVolumes(context);

        for (Foc cache : volumes.getCaches()) {
            final Foc tiles = cache.child(AppDirectory.DIR_TILES);
            add_w(list, cache, tiles);
        }


        for (Foc vol : volumes.getVolumes()) {
            final Foc osmdroid = vol.child(AppDirectory.DIR_TILES_OSMDROID);
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            add_w(list, osmdroid);
            add_w(list, aat);
        }


        for (Foc vol : volumes.getVolumes()) {
            final Foc osmdroid = vol.child(AppDirectory.DIR_TILES_OSMDROID);
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            add_ro(list, osmdroid);
            add_ro(list, aat);
        }

        for (Foc vol : volumes.getVolumes()) {
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            if (aat.exists()==false)
                add_ro(list, vol, aat);
        }

        return list;
    }

}
