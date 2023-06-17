package ch.bailu.aat.preferences.system;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat_lib.preferences.SelectionList;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroidFactory;

public class SolidExternalDirectory extends SolidFile {
    private final Context context;

    public SolidExternalDirectory(Context c) {
        super(new Storage(c), "ExternalDirectory", new FocAndroidFactory(c));
        this.context = c;
    }



    private final static String[] KNOWN_DIRS = {
            AppDirectory.DIR_AAT_DATA + AppDirectory.DIR_IMPORT,
            "MyTracks/gpx",
            "gpx"
    };


    public Context getContext() {
        return context;
    }

    @Override
    public String getLabel() {
        return Res.str().intro_external_list();
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        AndroidVolumes volumes = new AndroidVolumes(getContext());

        list.add(getContext().getString(R.string.none));



        for (String dir : KNOWN_DIRS) {
            for (Foc vol : volumes.getVolumes()) {
                SelectionList.add_w(list, vol.child(dir));
            }
        }

        for (String dir : KNOWN_DIRS) {
            for (Foc vol : volumes.getVolumes()) {
                SelectionList.add_ro(list, vol.child(dir));
            }
        }

        return list;
    }
}
