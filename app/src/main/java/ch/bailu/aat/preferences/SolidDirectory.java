package ch.bailu.aat.preferences;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;


public abstract class SolidDirectory extends SolidString {

    public SolidDirectory(Storage s, String k) {
        super(s, k);
    }


    public File getValueAsFile() {
        return new File(getValueAsString());
    }

    public int getIconResource() {return R.drawable.folder_inverse;}

    public abstract ArrayList<String> buildSelection(ArrayList<String> list);


}
