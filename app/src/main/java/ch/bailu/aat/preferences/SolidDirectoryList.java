package ch.bailu.aat.preferences;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

public abstract class SolidDirectoryList extends SolidIndexList {


    private final ArrayList<String> list = new ArrayList<String>(10);




    public SolidDirectoryList(Context c, String k) {
        super(Storage.preset(c), k);
        initList(list);
    }

    public abstract void initList(ArrayList<String> list);
    
    @Override
    public int length() {
        return list.size();
    }

    @Override
    public String getString() {
        final int index = Math.min(list.size()-1, getIndex());
        return list.get(index);
    }
    
    
    @Override
    public String toString() {
        return getString();
    }

    public File toFile() {
        return new File(getString());
    }
    

    @Override
    public String[] getStringArray() {
        return toStringArray(list);
    }

    private static String[] toStringArray(ArrayList<String> l) {
        String[] r = new String[l.size()];
        for (int i = 0; i<l.size(); i++)
            r[i]=l.get(i);

        return r;
    }

    
    public static void fillDirectoryList(ArrayList<String> list, String[] pf) {
        for (int i=0; i<pf.length; i++)
            fillList(list, pf[i]);
    }


    public static void  fillList(ArrayList<String> list, String pf) {
        addFileToList(list, Environment.getExternalStorageDirectory(), pf);
        addFileToList(list, Environment.getDataDirectory(), pf);
        addPathToList(list, "/mnt", pf);
    }


    public static void addPathToList(ArrayList<String> l, String p, String pf) {
        File files[] = new File(p).listFiles();
        if (files != null) {
            for (int i=0; i<files.length; i++)
                addFileToList(l, files[i], pf);
        }
    }


    public static void addFileToList(ArrayList<String> l, File f, String pf) {
        if (f.exists()) l.add(f.getAbsolutePath() + "/" + pf);
    }
    
    public static void addFileToList(ArrayList<String> l, File f) {
        if (f.exists()) l.add(f.getAbsolutePath());
    }
}
