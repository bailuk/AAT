package ch.bailu.aat_lib.gpx;

import static ch.bailu.foc.Foc.FOC_NULL;

import ch.bailu.foc.Foc;

public class GpxInformation extends GpxDataWrapper  {
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
