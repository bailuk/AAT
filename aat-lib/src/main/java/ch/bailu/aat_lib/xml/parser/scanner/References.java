package ch.bailu.aat_lib.xml.parser.scanner;

import java.util.HashMap;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.coordinates.LatLongE6;

public class References {

    public int id = 0;
    public int resolved = 0;
    public BoundingBoxE6 bounding = new BoundingBoxE6();

    private final HashMap<Integer, LatLongE6> coordinates = new HashMap<>(50);

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
