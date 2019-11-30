package ch.bailu.aat.services.dem.updater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ch.bailu.aat.coordinates.SrtmCoordinates;

public final class PendingUpdatesMap {
    private final HashMap<SrtmCoordinates,ArrayList<ElevationUpdaterClient>> map =
            new HashMap<>();



    public void add(SrtmCoordinates c , ElevationUpdaterClient e) {
        ArrayList<ElevationUpdaterClient> l = map.get(c);

        if (l==null) {
            l = new ArrayList<>(10);
            map.put(c, l);
        }

        if (!l.contains(e)) {
            l.add(e);
        }
    }


    public void remove(SrtmCoordinates s) {
        map.remove(s);
    }


    public void removeIfEmpty(SrtmCoordinates s) {
        ArrayList<ElevationUpdaterClient> l = map.get(s);

        if (l == null || l.isEmpty()) {
            remove(s);
        }
    }


    public void remove(ElevationUpdaterClient e) {
        Iterator<ArrayList<ElevationUpdaterClient>> values = map.values().iterator();

        while (values.hasNext()) {
            ArrayList<ElevationUpdaterClient> l = values.next();

            if (l != null) {
                l.remove(e);
            }
        }

        removeAllEmpty();
    }

    private void removeAllEmpty() {

        Iterator<SrtmCoordinates> c = coordinates();

        while (c.hasNext()) {
            removeIfEmpty(c.next());
        }
    }



    public Iterator<SrtmCoordinates> coordinates() {
        return new HashSet<>(map.keySet()).iterator();
    }


    public ArrayList<ElevationUpdaterClient> get(SrtmCoordinates c) {
        return map.get(c);
    }
}
