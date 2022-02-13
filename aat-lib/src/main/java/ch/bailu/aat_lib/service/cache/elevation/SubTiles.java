package ch.bailu.aat_lib.service.cache.elevation;


import java.util.ArrayList;

import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.service.cache.Span;
import ch.bailu.aat_lib.util.IndexedMap;

public final class SubTiles {
    private final IndexedMap<Dem3Coordinates, SubTile> subTiles = new IndexedMap<>();
    private Dem3Coordinates[] coordinates;

    private int inUse=0;

    public synchronized boolean haveID(String id) {

        for (int i = 0; i< subTiles.size(); i++) {
            final SubTile subTile = subTiles.getAt(i);
            if (subTile != null && id.contains(subTile.toString())) {
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
                coordinates[i] = subTiles.getAt(i).coordinates;
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
        subTiles.put(subTile.coordinates, subTile);
    }


    public synchronized SubTile take(Dem3Coordinates coordinates) {

        final SubTile r = subTiles.get(coordinates);

        if (r != null) {
            inUse++;
            subTiles.remove(coordinates);
        }
        return r;
    }


    public synchronized void done() {
        inUse--;
    }
}
