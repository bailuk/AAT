package ch.bailu.aat_lib.preferences;

import java.util.ArrayList;

import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.directory.GpxDbConstants;
import ch.bailu.foc.FocFactory;

public class SolidDirectoryQuery extends SolidFile {
    private static final String KEY_DIR_DIRECTORY="DIR_DIRECTORY";
    private static final String KEY_DIR_INDEX="DIR_INDEX_";

    private final static String KEY_DATE_START="DATE_START_";
    private final static String KEY_DATE_END="DATE_END_";
    private final static String KEY_USE_DATE_START="USE_DATE_START_";
    private final static String KEY_USE_DATE_END="USE_DATE_END_";

    private final static String KEY_USE_GEO="USE_GEO_";

    private final static String KEY_BOUNDING_BOX = "BOX_";


    public SolidDirectoryQuery(StorageInterface s, FocFactory f) {
        super(s, KEY_DIR_DIRECTORY, f);

    }

    public boolean containsKey(String s) {
        return s.contains(getValueAsString());
    }

    public SolidInteger getPosition() {
        return new SolidInteger(getStorage(), KEY_DIR_INDEX+ getValueAsString());
    }


    public SolidBoolean getUseDateStart() {
        return new SolidBoolean(getStorage(), KEY_USE_DATE_START+ getValueAsString());
    }


    public SolidBoolean getUseDateEnd() {
        return new SolidBoolean(getStorage(), KEY_USE_DATE_END+ getValueAsString());
    }


    public SolidDate getDateStart() {
        return new SolidDate(
                getStorage(),
                KEY_DATE_START+ getValueAsString(),
                Res.str().filter_date_start());

    }


    public SolidDate getDateTo() {
        return new SolidDate(
                getStorage(),
                KEY_DATE_END+ getValueAsString(),
                Res.str().filter_date_to());
    }

    public SolidBoolean getUseGeo() {
        return new SolidBoolean(getStorage(), KEY_USE_GEO+ getValueAsString());

    }

    public SolidBoundingBox getBoundingBox() {
        return new SolidBoundingBox(
                getStorage(),
                KEY_BOUNDING_BOX+ getValueAsString(),
                 Res.str().filter_geo());
    }



    public String createSelectionString() {
        final String d = createSelectionStringDate();
        final String b = createSelectionStringBounding();

        if (d.length()>0 && b.length()>0) {
            return "(" + d + ") AND (" + b + ")";
        } else {
            return d + b;
        }
    }


    public String createSelectionStringBounding() {
        if (getUseGeo().getValue()) {
            return getBoundingBox().createSelectionStringInside();
        }

        return "";
    }


    private final static long DAY = 1000*60*60*24; // ms*sec*min*h = d
    public String createSelectionStringDate() {
        String selection="";


        if (getUseDateStart().getValue() || getUseDateEnd().getValue()) {
            long start, end;

            if (getUseDateStart().getValue()) {
                start = getDateStart().getValue();
            } else {
                start = 0;
            }

            if (getUseDateEnd().getValue()) {
                end = getDateTo().getValue();
            } else {
                end = System.currentTimeMillis()/DAY;
                end = end + 5;
                end = end * DAY;
            }


            selection = GpxDbConstants.KEY_START_TIME
                    + " BETWEEN "
                    + Math.min(start, end)
                    + " AND "
                    + Math.max(start, end);
        }

        return selection;
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        return null;
    }
}



