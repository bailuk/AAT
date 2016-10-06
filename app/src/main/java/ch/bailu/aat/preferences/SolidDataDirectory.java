package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;

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
            r = new OldSolidDataDirectory(getContext()).getValueAsString();
        }
        return r;
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        File internal = getContext().getFilesDir();
        File external = getContext().getExternalFilesDir(null);
        File aat_data = new File(Environment.getExternalStorageDirectory(),"aat_data/");

        list.add(internal.getAbsolutePath());
        list.add(external.getAbsolutePath());
        list.add(aat_data.getAbsolutePath());

        return list;
    }
}

