package ch.bailu.aat.services.cache.elevation;

import android.util.SparseArray;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.Dem3Coordinates;
import ch.bailu.aat.services.cache.Span;

public final class SubTiles {
    private final SparseArray<SubTile> subTiles = new SparseArray<>(25);
    private Dem3Coordinates[] coordinates;

    private int inUse=0;

    public synchronized boolean haveID(String id) {

        for (int i = 0; i< subTiles.size(); i++) {
            if (id.contains(subTiles.valueAt(i).toString())) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isNotPainting() {return inUse==0;}
    public synchronized boolean areAllPainted() {return isNotPainting() && subTiles.size() == 0;}

    public synchronized Dem3Coordinates[] toSrtmCoordinates() {
        if (coordinates == null || coordinates.length != subTiles.size()) {

            coordinates = new Dem3Coordinates[subTiles.size()];

            for (int i = 0; i < subTiles.size(); i++) {
                coordinates[i] = subTiles.valueAt(i).coordinates;
            }
        }
        return coordinates;
    }


    public synchronized void generateSubTileList(ArrayList<Span> laSpan, ArrayList<Span> loSpan) {
        for (int la=0; la<laSpan.size(); la++) {
            for (int lo=0; lo<loSpan.size(); lo++) {
                put(laSpan.get(la), loSpan.get(lo));
            }
        }
    }


    private void put(Span la, Span lo) {
        SubTile subTile = new SubTile(la, lo);
        subTiles.put(subTile.hashCode(), subTile);
    }


    public synchronized SubTile take(int key) {

        final SubTile r = subTiles.get(key);

        if (r != null) {
            inUse++;
            subTiles.remove(key);
        }
        return r;
    }


    public synchronized void done() {
        inUse--;
    }


}
