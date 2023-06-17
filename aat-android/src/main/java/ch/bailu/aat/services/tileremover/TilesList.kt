package ch.bailu.aat.services.tileremover;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public final class TilesList {
    private final static int FILES_LIMIT=50000;


    private final TreeSet<TileFile> files = new TreeSet<>(

            (o1, o2) -> Long.compare(o1.lastModified(), o2.lastModified()));

    private  final ArrayList<TileFile> filesToRemove = new ArrayList<>(FILES_LIMIT);


    public void resetToRemove() {
        filesToRemove.clear();
    }

    public void add(TileFile file) {
        files.add(file);

        /*
        if (!files.add(file)) {
            AppLog.d(this, file.getSource() +"/"+ file.toString());
        }*/
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

    /*
    public void log() {
        AppLog.d(this, "Files: " + files.size());
    }
    */
}
