package ch.bailu.aat.preferences.system;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public class SolidExternalDirectory extends SolidFile {
    public SolidExternalDirectory(Context c) {
        super(c, "ExternalDirectory");
    }

    private final static String[] KNOWN_DIRS = {
            AppDirectory.DIR_AAT_DATA + AppDirectory.DIR_IMPORT,
            "MyTracks/gpx",
            "gpx"
    };


    @Override
    public String getLabel() {
        return getContext().getString(R.string.intro_external_list);
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        AndroidVolumes volumes = new AndroidVolumes(getContext());

        list.add(getContext().getString(R.string.none));



        for (String dir : KNOWN_DIRS) {
            for (Foc vol : volumes.getVolumes()) {
                add_w(list, vol.child(dir));
            }
        }

        for (String dir : KNOWN_DIRS) {
            for (Foc vol : volumes.getVolumes()) {
                add_ro(list, vol.child(dir));
            }
        }

        return list;
    }
}
