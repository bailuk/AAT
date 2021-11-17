package ch.bailu.aat_lib.preferences;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.service.directory.GpxDbConstants;

public class SolidBoundingBox implements SolidTypeInterface {
    private final SolidInteger N, W, S, E;


    private final String label;

    public SolidBoundingBox(final StorageInterface s, final String key, String l) {
        label = l;

        N = new SolidInteger(s, key + "_N");
        E = new SolidInteger(s, key + "_E");
        S = new SolidInteger(s, key + "_S");
        W = new SolidInteger(s, key + "_W");
    }

    public BoundingBoxE6 getValue() {
        return new BoundingBoxE6(
                N.getValue(),
                E.getValue(),
                S.getValue(),
                W.getValue());
    }

    public void setValue(BoundingBoxE6 b) {
        N.setValue(b.getLatNorthE6());
        E.setValue(b.getLonEastE6());
        S.setValue(b.getLatSouthE6());
        W.setValue(b.getLonWestE6());
    }


    public boolean hasKey(String k) {
        return N.hasKey(k) || E.hasKey(k) || S.hasKey(k) || W.hasKey(k);
    }

    @Override
    public void register(OnPreferencesChanged listener) {
        N.register(listener);
    }

    @Override
    public void unregister(OnPreferencesChanged listener) {
        N.unregister(listener);
    }


    public String getValueAsString() {
        return getValue().toString();
    }



    @Override
    public String getKey() {
        return N.getKey().substring(0,N.getKey().length()-3);
    }

    @Override
    public StorageInterface getStorage() {
        return N.getStorage();
    }


    @Override
    public String getLabel() {
        return label;
    }


    public String createSelectionStringOverlaps() {

        final int n = N.getValue(), e = E.getValue(), s = S.getValue(), w = W.getValue();

        return
         "(("
        +          GpxDbConstants.KEY_NORTH_BOUNDING  + " < " + n
        +" AND " + GpxDbConstants.KEY_NORTH_BOUNDING  + " > " + s

        +") OR ("

        +          GpxDbConstants.KEY_SOUTH_BOUNDING  + " < " + n
        +" AND " + GpxDbConstants.KEY_SOUTH_BOUNDING  + " > " + s
        +"))"


        + " AND "


        +"(("
        +" AND " + GpxDbConstants.KEY_EAST_BOUNDING   + " > " + w
        +" AND " + GpxDbConstants.KEY_EAST_BOUNDING   + " < " + e

        +") OR ("

        +" AND " + GpxDbConstants.KEY_WEST_BOUNDING   + " > " + w
        +" AND " + GpxDbConstants.KEY_WEST_BOUNDING   + " < " + e
        +"))";

    }


    public String createSelectionStringInside() {
        final int n = N.getValue(), e = E.getValue(), s = S.getValue(), w = W.getValue();

        return    GpxDbConstants.KEY_NORTH_BOUNDING  + " < " + n +
        " AND " + GpxDbConstants.KEY_SOUTH_BOUNDING  + " > " + s +
        " AND " + GpxDbConstants.KEY_EAST_BOUNDING   + " < " + e +
        " AND " + GpxDbConstants.KEY_WEST_BOUNDING   + " > " + w;
    }

    @Override
    public String getToolTip() {
        return null;
    }
}
