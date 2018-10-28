package ch.bailu.aat.preferences;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.util_java.util.Objects;

public class SolidDataDirectory extends SolidFile {

    private final SolidDataDirectoryDefault defaultDirectory;

    public SolidDataDirectory(Context c) {
        super(c, SolidDataDirectory.class.getSimpleName());
        defaultDirectory = new SolidDataDirectoryDefault(getContext());
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_directory_data);
    }


    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();


        if (Objects.equals(r,Storage.DEF_VALUE))
            return defaultDirectory.getValueAsString();

        return r;
    }

    @Override
    public boolean hasKey(String s) {
        return super.hasKey(s) || defaultDirectory.hasKey(s);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        return defaultDirectory.buildSelection(list);
    }

}

