package ch.bailu.aat.preferences;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.AndroidVolumes;

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

        add_w(list, getContext().getExternalFilesDir(null));

        if (list.size()>0) {
            return list.get(0);
        }
        return "";
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        AndroidVolumes volumes = new AndroidVolumes(getContext());

        for (File f : volumes.getVolumes()) {
            final File maps1 = new File(f, MAPS_DIR);
            final File maps2 = new File(f,
                    AppDirectory.DIR_AAT_DATA + "/" + MAPS_DIR);


            add_r(list, maps1);
            add_r(list, maps2);
        }

        return list;
    }
}
