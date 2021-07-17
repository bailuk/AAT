package ch.bailu.aat_lib.gpx;

import ch.bailu.foc.Foc;
import ch.bailu.foc.FocName;

public class GpxInformation extends GpxDataWrapper  {

    // TODO move elsewhere
    public static final Foc FOC_NULL = new FocName("");

    public final static GpxInformation NULL=new GpxInformation();

    public boolean isLoaded() {
        return false;
    }

    public GpxList getGpxList() {
        return GpxList.NULL_TRACK;
    }

    public float getAccuracy() {
        return 0f;
    }

    public int getState() {return StateID.ON;}

    public Foc getFile() {return FOC_NULL; }
}