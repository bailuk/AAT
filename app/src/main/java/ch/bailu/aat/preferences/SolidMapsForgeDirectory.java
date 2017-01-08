package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.util.fs.AppDirectory;

public class SolidMapsForgeDirectory extends SolidDirectory {
    public final static String MAPS_DIR="maps";

    private final static String KEY = SolidMapsForgeDirectory.class.getSimpleName();

    public SolidMapsForgeDirectory(Context c) {
        super(Storage.global(c), KEY);
    }

    @Override
    public String getLabel() {
        return "Offline maps directory*";
    }




    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();


        if (r.equals(Storage.DEF_VALUE)) {
            r = getDefaultValue();

            setValue(r);
        }
        return r;
    }


    public String getDefaultValue() {
        ArrayList<String> list = new ArrayList<>(5);

        list = buildSelection(list);

        add(list, getContext().getExternalFilesDir(null));

        if (list.size()>0) {
            return list.get(0);
        }
        return "";
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        File external = getContext().getExternalFilesDir(null);
        if (external != null) {
            final File external_maps = new File(external, MAPS_DIR);
            add(list, external_maps);
        }


        final File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard != null) {
            final File sdcard_maps1 = new File(sdcard, MAPS_DIR);
            final File sdcard_maps2 = new File(sdcard,
                    AppDirectory.DIR_AAT_DATA + "/" + MAPS_DIR);

            add(list, sdcard_maps1);
            add(list, sdcard_maps2);
        }

        return list;
    }
}
