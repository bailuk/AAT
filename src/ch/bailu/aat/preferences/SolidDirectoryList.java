package ch.bailu.aat.preferences;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

public class SolidDirectoryList extends SolidIndexList {


    private String[] list = {""};


    private String[] createValueList(Context c, String[] pf) {
        ArrayList<String> l = new ArrayList<String>();

        for (int i=0; i<pf.length; i++)
            fillList(l, pf[i]);

        return toStringArray(l);
    }


    private String[] toStringArray(ArrayList<String> l) {
        String[] r = new String[l.size()];
        for (int i = 0; i<l.size(); i++)
            r[i]=l.get(i);

        return r;
    }


    private void fillList(ArrayList<String> l, String pf) {
        addFileToList(l, Environment.getExternalStorageDirectory(), pf);
        addFileToList(l, Environment.getDataDirectory(), pf);
        addPathToList(l, "/mnt", pf);
    }


    private void addPathToList(ArrayList<String> l, String p, String pf) {
        File files[] = new File(p).listFiles();
        if (files != null) {
            for (int i=0; i<files.length; i++)
                addFileToList(l, files[i], pf);
        }
    }


    private void addFileToList(ArrayList<String> l, File f, String pf) {
        if (f.exists())
            l.add(f.getAbsolutePath()+"/"+pf);
    }


    public SolidDirectoryList(Context c, String k, String[] pf) {
        super(Storage.preset(c), k);
        if (list.length == 1) list = createValueList(c, pf);
    }

    @Override
    public int length() {
        return list.length;
    }

    @Override
    public String getString() {
        return list[this.getIndex()];
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
        return list;
    }

}
