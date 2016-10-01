package ch.bailu.aat.services.tileremover;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class TilesList {
    public final static int FILES_LIMIT=50000;


    private final PriorityQueue<TileFile> files = new PriorityQueue<>(
            FILES_LIMIT,
            new Comparator<TileFile>() {
        @Override
        public int compare(TileFile o1, TileFile o2) {
            return (int) (o2.lastModified()-o1.lastModified());
        }
    });

    private  final ArrayList<TileFile> filesToRemove = new ArrayList<TileFile>(FILES_LIMIT);


    public void resetToRemove() {
        filesToRemove.clear();
    }

    public void add(TileFile file) {
        files.add(file);
        if (files.size()>= FILES_LIMIT) {
            files.poll();
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
}
