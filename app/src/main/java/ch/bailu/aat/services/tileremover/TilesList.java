package ch.bailu.aat.services.tileremover;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import ch.bailu.aat.util.ui.AppLog;

public class TilesList {
    private final static int FILES_LIMIT=50000;


    private final TreeSet<TileFile> files = new TreeSet<>(

            new Comparator<TileFile>() {
        @Override
        public int compare(TileFile o1, TileFile o2) {

            //if (o1.equals(o2)) return 0;

            if (o2.lastModified() > o1.lastModified())
                return -1;

            else
                return 1;
        }
    });

    private  final ArrayList<TileFile> filesToRemove = new ArrayList<>(FILES_LIMIT);


    public void resetToRemove() {
        filesToRemove.clear();
    }

    public void add(TileFile file) {
        if (!files.add(file)) {
            AppLog.d(this, file.getSource() +"/"+ file.toString());
        }
        if (files.size()>= FILES_LIMIT) {
            files.pollLast();
        }
    }

    public void addToRemove(TileFile file) {
        filesToRemove.add(file);
    }


    public Iterator<TileFile> iteratorToRemove() {
        return filesToRemove.iterator();
    }


    public Iterator<TileFile> iterator() {
        return files.iterator();
    }

    public void log() {
        AppLog.d(this, "Files: " + files.size());
    }
}
