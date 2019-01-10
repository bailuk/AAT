package ch.bailu.aat.gpx.xml_parser.scanner;

import android.util.SparseArray;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6;

public class References {

    public int id = 0;
    public int resolved = 0;
    public BoundingBoxE6 bounding = new BoundingBoxE6();

    private final SparseArray<LatLongE6> coordinates = new SparseArray<>(50);


    public void clear() {
        bounding = new BoundingBoxE6();
        id = 0;
        resolved = 0;
    }

    public void put(int id, LatLongE6 c) {
        coordinates.put(id, c);
    }

    public LatLongE6 get(int key) {
        return coordinates.get(key);
    }
}
