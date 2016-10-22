package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;

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

        add(list, f);

        if (list.size()==0)
            list = buildSelection(list);

        if (list.size()==0)
            list.add(f.getAbsolutePath());

        return list.get(0);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        File internal = getContext().getFilesDir();
        File external = getContext().getExternalFilesDir(null);
        File sdcard   = Environment.getExternalStorageDirectory();
        File aat_data = new File(sdcard, AppDirectory.DIR_AAT_DATA);

        add(list, external);
        add(list, sdcard, aat_data);
        add(list, internal);

        return list;
    }


}

