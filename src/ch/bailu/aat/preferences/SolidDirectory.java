package ch.bailu.aat.preferences;

import android.content.Context;
import ch.bailu.aat.services.directory.GpxDbConstants;

public class SolidDirectory extends SolidString {
    private static final String KEY_DIR_DIRECTORY="DIR_DIRECTORY";
    private static final String KEY_DIR_INDEX="DIR_INDEX_";

    private final static String KEY_DATE_START="DATE_START_";
    private final static String KEY_DATE_END="DATE_END_";
    private final static String KEY_USE_DATE_START="USE_DATE_START_";
    private final static String KEY_USE_DATE_END="USE_DATE_END_";

    private final static String KEY_USE_GEO="USE_GEO_";



    public SolidDirectory(Context c) {
        super(Storage.global(c), KEY_DIR_DIRECTORY);

    }

    public boolean containsKey(String s) {
        return s.contains(getValue());
    }
    
    public SolidInteger getPosition() {
        return new SolidInteger(getStorage(), KEY_DIR_INDEX+getValue());
    }


    public SolidBoolean getUseDateStart() {
        return new SolidBoolean(getStorage(), KEY_USE_DATE_START+getValue());
    }


    public SolidBoolean getUseDateEnd() {
        return new SolidBoolean(getStorage(), KEY_USE_DATE_END+getValue());
    }


    public SolidLong getDateStart() {
        return new SolidLong(getStorage(), KEY_DATE_START+getValue());
    }


    public SolidLong getDateEnd() {
        return new SolidLong(getStorage(), KEY_DATE_END+getValue());
    }

    public SolidBoolean getUseGeo() {
        return new SolidBoolean(getStorage(), KEY_USE_GEO+getValue());

    }


    private final static long DAY = 1000*60*60*24; // ms*sec*min*h = d
    public String createSelectionString() {
        String selection="";


        if (getUseDateStart().getValue() || getUseDateEnd().getValue()) {
            long start, end;

            if (getUseDateStart().getValue()) {
                start = getDateStart().getValue();
            } else {
                start = 0;
            }

            if (getUseDateEnd().getValue()) {
                end = getDateEnd().getValue();
            } else {
                end = System.currentTimeMillis()/DAY;
                end = end + 5;
                end = end * DAY;
            }


            selection = GpxDbConstants.KEY_START_TIME 
                    + " BETWEEN " 
                    + String.valueOf(Math.min(start, end)) 
                    + " AND " 
                    + String.valueOf(Math.max(start, end)) ;
        }

        return selection;
    }
}



