package ch.bailu.aat.preferences;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.JFile;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.simpleio.foc.Foc;


public abstract class SolidFile extends SolidString {

    public SolidFile(Storage s, String k) {
        super(s, k);
    }


    public Foc getValueAsFile() {
        return FocAndroid.factory(getContext(), getValueAsString());
    }
    public int getIconResource() {return R.drawable.folder_inverse;}

    public abstract ArrayList<String> buildSelection(ArrayList<String> list);


    public static void add_ro(ArrayList<String> list, Foc file) {
        add_ro(list, file, file);
    }

    public static void add_ro(ArrayList<String> list, Foc check, Foc file) {
        if (check.canOnlyRead()) {
            list.add(file.toString());
        }
    }

    public static void add_r(ArrayList<String> list, Foc file) {
        if (file.canRead())
            list.add(file.toString());
    }

    public static void add_subdirectories_r(final ArrayList<String> list, Foc directory) {
        directory.foreachDir(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                add_r(list, child);
            }
        });
    }


    public static void add_w(ArrayList<String> list, Foc file) {
        add_w(list, file, file);

    }


    public static void add_w(ArrayList<String> list, Foc check, Foc file) {
        if (file != null && check.canWrite())  {
            list.add(file.toString());
        }
    }



}
