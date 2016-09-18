package ch.bailu.aat.gpx;




public class GpxInformation extends GpxDataWrapper  {
    private final static String NULL_NAME="";

    
    public class ID {
        public final static int INFO_ID_ALL = 0;
        
        public final static int INFO_ID_LOCATION = 1;
        public final static int INFO_ID_FILEVIEW = 2;
        public final static int INFO_ID_TRACKER = 3;
        
        public final static int INFO_ID_EDITOR_OVERLAY = 40;
        public final static int INFO_ID_EDITOR_DRAFT = 41;
        
        public final static int INFO_ID_LIST_SUMMARY = 5;
        
        public final static int INFO_ID_OVERLAY=60;
        
        public final static int INFO_ID_UNSPECIFIED = -1;

    
        public final static int STATE_ON=0;
        public final static int STATE_OFF=1;
        public final static int STATE_PAUSE=2;
        public final static int STATE_AUTOPAUSED=6;
        public final static int STATE_WAIT=5;
    
        public final static int STATE_NOACCESS=3;
        public final static int STATE_NOSERVICE=4;
    }
    

    public int getID() {
        return ID.INFO_ID_UNSPECIFIED;
    }

    
    
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
    
    public int getState() {return ID.STATE_ON;}
    
    
    public String getPath() {
        return NULL_NAME;
    }

    
    public String getName() {
        return NULL_NAME;
    }
}