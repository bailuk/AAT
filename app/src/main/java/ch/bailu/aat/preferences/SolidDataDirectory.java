package ch.bailu.aat.preferences;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.AndroidVolumes;

public class SolidDataDirectory extends SolidDirectory {

    public SolidDataDirectory(Context c) {
        super(Storage.global(c), SolidDataDirectory.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_directory_data);
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

    private String getDefaultValue() {
        final File f = new OldSolidDataDirectory(getContext()).toFile();

        ArrayList<String> list = new ArrayList<>(5);

        add_w(list, f);

        if (list.size()==0)
            list = buildSelection(list);

        if (list.size()==0)
            list.add(f.getAbsolutePath());

        return list.get(0);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        AndroidVolumes volumes = new AndroidVolumes(getContext());

        for (File vol : volumes.getVolumes()) {
            File aat_data = new File(vol, AppDirectory.DIR_AAT_DATA);
            add_w(list, aat_data);
        }

        for (File vol : volumes.getVolumes()) {
            File aat_data = new File(vol, AppDirectory.DIR_AAT_DATA);
            if (aat_data.exists()==false)
                add_w(list, vol, aat_data);
        }

        File[] files = volumes.getFiles();
        for (int i=1; i<files.length; i++) {
            add_w(list, files[i]);
        }

        for (int i=1; i<files.length; i++) {
            add_ro(list, files[i]);
        }

        for (File vol : volumes.getVolumes()) {
            File aat_data = new File(vol, AppDirectory.DIR_AAT_DATA);
            if (aat_data.exists()==false)
                add_ro(list, vol, aat_data);
        }

        if (files.length>0) add_w(list, files[0]);
        return list;
    }


}

