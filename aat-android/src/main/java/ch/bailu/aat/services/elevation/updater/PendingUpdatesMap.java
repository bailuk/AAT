package ch.bailu.aat.services.elevation.updater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient;

public final class PendingUpdatesMap {
    private final HashMap<Dem3Coordinates,ArrayList<ElevationUpdaterClient>> map =
            new HashMap<>();



    public void add(Dem3Coordinates c , ElevationUpdaterClient e) {
        ArrayList<ElevationUpdaterClient> l = map.get(c);

        if (l==null) {
            l = new ArrayList<>(10);
            map.put(c, l);
        }

        if (!l.contains(e)) {
            l.add(e);
        }
    }


    public void remove(Dem3Coordinates s) {
        map.remove(s);
    }


    public void removeIfEmpty(Dem3Coordinates s) {
        ArrayList<ElevationUpdaterClient> l = map.get(s);

        if (l == null || l.isEmpty()) {
            remove(s);
        }
    }


    public void remove(ElevationUpdaterClient e) {

        for (ArrayList<ElevationUpdaterClient> l : map.values()) {
            if (l != null) {
                l.remove(e);
            }
        }

        removeAllEmpty();
    }

    private void removeAllEmpty() {

        Iterator<Dem3Coordinates> c = coordinates();

        while (c.hasNext()) {
            removeIfEmpty(c.next());
        }
    }



    public Iterator<Dem3Coordinates> coordinates() {
        return new HashSet<>(map.keySet()).iterator();
    }


    public ArrayList<ElevationUpdaterClient> get(Dem3Coordinates c) {
        return map.get(c);
    }
}
