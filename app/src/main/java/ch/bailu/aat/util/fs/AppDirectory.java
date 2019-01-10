package ch.bailu.aat.util.fs;

import android.content.Context;

import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.preferences.system.SolidDataDirectory;
import ch.bailu.aat.preferences.map.SolidTileCacheDirectory;
import ch.bailu.util_java.foc.Foc;

public class AppDirectory  {

    //////////////////////////////////////////////////////////////////////////////////////
    public static Foc getDataDirectory(Context c, String sub) {
        return new SolidDataDirectory(c).getValueAsFile().descendant(sub);
    }

    public static final String DIR_AAT_DATA = "aat_data";
    public static final String DIR_TILES = "tiles";
    public static final String DIR_TILES_OSMDROID = "osmdroid/tiles";

    
    public static final String DIR_LOG = "log";
    public static final String FILE_LOG = "log.gpx";
    public static Foc getLogFile(Context context) {
        return getDataDirectory(context, DIR_LOG).child(FILE_LOG);
    }
    
    
    public static final String DIR_OVERLAY = "overlay";
    public static final String DIR_IMPORT = "import";
    
    public static final String DIR_NOMINATIM = "nominatim";

    public static final String DIR_OVERPASS = "overpass";

    
    public static final String DIR_TEST = "test";
    
    
    

    
    public static final String DIR_CACHE = "cache";
    public static final String FILE_CACHE_DB="summary.db";
    


    public static final String DIR_EDIT = "overlay/draft";
    public static final String FILE_DRAFT= "draft.gpx";

    public static Foc getEditorDraft(Context c) {
        return getDataDirectory(c, DIR_EDIT).child(FILE_DRAFT);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    
    

    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public static Foc getTileFile(String tilePath, Context context) {
        return getTileCacheDirectory(context).descendant(tilePath);
    }


    private static Foc getTileCacheDirectory(Context c) {
        return new SolidTileCacheDirectory(c).getValueAsFile();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    

    
    ///////////////////////////////////////////////////////////////////////////////////////
    private static final String PRESET_PREFIX = "activity";
    public static final String PREVIEW_EXTENSION = ".preview";

    static public Foc getTrackListDirectory(Context c, int i) {
        return getDataDirectory(c,getPresetPrefix(i));
    }
    static private String getPresetPrefix(int i) {
        return PRESET_PREFIX + i;
    }
    
    


    

    ////////////////////////////////////////////////////////////////////////////////////////


    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final String GPX_EXTENSION=".gpx";
    public static final String OSM_EXTENSION=".osm";

    public static Foc generateUniqueFilePath(Foc directory, String prefix, String extension)
            throws IOException {
        Foc file;

        int x=0;
        do {
            file = directory.child(generateFileName(prefix, x, extension));
            x++;
        } while (file.exists() && x < 999);

        if (file.exists()) throw new IOException();

        return file;
    }


    
    public static String generateDatePrefix() {
        long time = System.currentTimeMillis();
        return String.format(
                (Locale) null, 
                "%tY_%tm_%td", time,time,time);
    }


    private static String generateFileName(String prefix, int i, String extension) {
        return String.format(
                (Locale) null, 
                "%s_%d%s", prefix, i, extension);
    }



    public static String parsePrefix(Foc file) {
        StringBuilder name = new StringBuilder(file.getName());

        int length=name.length();

        for (int i=length; i>0 ; i--) { 
            if (name.charAt(i-1)=='.') {
                length=i;
                break;
            }
        }

        name.setLength(length);

        return name.toString();
    }

}
