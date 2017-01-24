package ch.bailu.aat.services.tileremover;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class TilesList {
    private final static int FILES_LIMIT=100000;


    private final TreeSet<TileFile> files = new TreeSet<>(

            new Comparator<TileFile>() {
        @Override
        public int compare(TileFile o1, TileFile o2) {
            if (o2.lastModified() > o1.lastModified())
                return -1;

            else if (o2.lastModified() < o1.lastModified())
                return 1;

            else return 0;
        }
    });

    private  final ArrayList<TileFile> filesToRemove = new ArrayList(FILES_LIMIT);


    public void resetToRemove() {
        filesToRemove.clear();
    }

    public void add(TileFile file) {
        files.add(file);
//        if (files.size()>= FILES_LIMIT) {
//            files.pollLast();
//        }
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
}
