package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.JFile;
import ch.bailu.aat.util.ui.AppLog;


public abstract class SolidDirectory extends SolidString {

    public SolidDirectory(Storage s, String k) {
        super(s, k);
    }


    public File getValueAsFile() {
        return new File(getValueAsString());
    }
    public int getIconResource() {return R.drawable.folder_inverse;}

    public abstract ArrayList<String> buildSelection(ArrayList<String> list);


    public static void add_ro(ArrayList<String> list, File file) {
        add_ro(list, file, file);
    }

    public static void add_ro(ArrayList<String> list, File check, File file) {
        if (JFile.canOnlyRead(check)) {
            list.add(file.getAbsolutePath());
        }
    }

    public static void add_r(ArrayList<String> list, File file) {
        if (JFile.canRead(file))
            list.add(file.getAbsolutePath());
    }


    public static void add_w(ArrayList<String> list, File file) {
        add_w(list, file, file);

    }


    public static void add_w(ArrayList<String> list, File check, File file) {
        if (file != null && JFile.canWrite(check))  {
            list.add(file.getAbsolutePath());
        }
    }



}
