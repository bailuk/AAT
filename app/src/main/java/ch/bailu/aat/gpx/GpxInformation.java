package ch.bailu.aat.gpx;




public class GpxInformation extends GpxDataWrapper  {
    private final static String NULL_NAME="";

    //public int getID() {
    //    return InfoID.UNSPECIFIED;
    //}



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


    public String getPath() {
        return NULL_NAME;
    }


    public String getName() {
        return NULL_NAME;
    }
}