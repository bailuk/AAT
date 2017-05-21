package ch.bailu.aat.services.cache.elevation;

import android.util.SparseArray;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.cache.Span;

public class SubTiles {
    private final SparseArray<SubTile> subTiles = new SparseArray<>(25);


    public boolean haveID(String id) {

        for (int i = 0; i< subTiles.size(); i++) {
            if (id.contains(subTiles.valueAt(i).toString())) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return subTiles.size();
    }

    public SrtmCoordinates[] toSrtmCoordinates() {
        final SrtmCoordinates c[] = new SrtmCoordinates[subTiles.size()];

        for (int i = 0; i< subTiles.size(); i++) {
            c[i]= subTiles.valueAt(i).coordinates;
        }
        return c;
    }


    public void generateSubTileList(ArrayList<Span> laSpan, ArrayList<Span> loSpan) {
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

    public SubTile get(int key) {
        return subTiles.get(key);
    }

    public void remove(int key) {
        subTiles.remove(key);
    }
}
