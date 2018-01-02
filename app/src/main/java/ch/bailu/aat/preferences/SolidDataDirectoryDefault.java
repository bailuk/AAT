package ch.bailu.aat.preferences;


import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.util.Objects;

public class SolidDataDirectoryDefault extends SolidFile {
    private static final String[] POSTFIX={"aat_data"};

    public SolidDataDirectoryDefault(Context c) {
        super(Storage.global(c), SolidDataDirectoryDefault.class.getSimpleName());
    }


    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();

          if (Objects.equals(r,Storage.DEF_VALUE))
            return setDefaultValue();

        return r;
    }


    public String setDefaultValue() {
        String r = getDefaultValue();
        setValue(r);
        return r;
    }

    private String getDefaultValue() {
        ArrayList<String> list = new ArrayList<>(5);
        list = buildSelection(list);
        list.add(Storage.DEF_VALUE);  // failsave
        return list.get(0);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        AndroidVolumes volumes = new AndroidVolumes(getContext());


        // volume/aat_data (exists and is writeable)
        for (Foc vol : volumes.getVolumes()) {
            Foc aat_data = vol.child(AppDirectory.DIR_AAT_DATA);
            add_w(list, aat_data);
        }

        // volume/aat_data (does not exist but can be created)
        for (Foc vol : volumes.getVolumes()) {
            Foc aat_data = vol.child(AppDirectory.DIR_AAT_DATA);
            if (aat_data.exists()==false)
                add_w(list, vol, aat_data);
        }

        // app_private/files (writeable and on external medium)
        Foc[] files = volumes.getFiles();
        for (int i=1; i<files.length; i++) {
            add_w(list, files[i]);
        }

        // app_private/files (read only and on external medium)
        for (int i=1; i<files.length; i++) {
            add_ro(list, files[i]);
        }

        // volume/aat_data (read only)
        for (Foc vol : volumes.getVolumes()) {
            Foc aat_data = vol.child(AppDirectory.DIR_AAT_DATA);
            add_ro(list, vol, aat_data);
        }

        // app_private/files (readable and internal)
        if (files.length>0) add_r(list, files[0]);

        return list;
    }

}
