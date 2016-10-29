package ch.bailu.aat.preferences;

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


    public static void add(ArrayList<String> list, File file) {
        add(list, file, file);

    }

    public static void add(ArrayList<String> list, File check, File file) {
        if (canWrite(check))  {
            list.add(file.getAbsolutePath());
        }
    }

    public static boolean canWrite(File check) {
        try {
            return check.canWrite();
        } catch (SecurityException e) {
            return false;
        }
    }
}
