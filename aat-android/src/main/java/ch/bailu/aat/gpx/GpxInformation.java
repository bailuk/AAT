package ch.bailu.aat.gpx;


import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

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

    public Foc getFile() {return FocAndroid.NULL; }
}