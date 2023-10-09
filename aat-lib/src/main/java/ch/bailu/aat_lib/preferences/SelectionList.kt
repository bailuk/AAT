package ch.bailu.aat_lib.preferences;

import java.util.ArrayList;

import ch.bailu.foc.Foc;

public class SelectionList {

    public static void add_dr(ArrayList<Foc> dirs, Foc file) {
        if (file.canRead() && file.isDir()) {
            dirs.add(file);
        }
    }

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
        directory.foreachDir(child -> add_r(list, child));
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
