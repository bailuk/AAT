package ch.bailu.aat_lib.util.fs;

import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.foc.Foc;

public class AppDirectory  {

    //////////////////////////////////////////////////////////////////////////////////////
    public static Foc getDataDirectory(SolidDataDirectory sdirectory, String sub) {
        return sdirectory.getValueAsFile().descendant(sub);
    }

    public static final String DIR_AAT_DATA = "aat_data";
    public static final String DIR_TILES = "tiles";
    public static final String DIR_TILES_OSMDROID = "osmdroid/tiles";


    private static final String DIR_LOG = "log";
    private static final String FILE_LOG = "log.gpx";
    public static Foc getLogFile(SolidDataDirectory sdirectory) {
        return getDataDirectory(sdirectory, DIR_LOG).child(FILE_LOG);
    }


    public static final String DIR_OVERLAY = "overlay";
    public static final String DIR_IMPORT = "import";

    public static final String DIR_NOMINATIM = "query/nominatim";
    public static final String DIR_OVERPASS = "query/overpass";
    public static final String DIR_POI = "query/poi";


    public static final String DIR_TEST = "test";





    public static final String DIR_CACHE = "cache";
    public static final String FILE_CACHE_DB="summary.db";



    private static final String DIR_EDIT = "overlay/draft";
    private static final String FILE_DRAFT= "draft.gpx";

    public static Foc getEditorDraft(SolidDataDirectory sdirectory) {
        return getDataDirectory(sdirectory, DIR_EDIT).child(FILE_DRAFT);
    }

    //////////////////////////////////////////////////////////////////////////////////////




    /////////////////////////////////////////////////////////////////////////////////////////////////
    public static Foc getTileFile(String tilePath, SolidTileCacheDirectory sdirectory) {
        return getTileCacheDirectory(sdirectory).descendant(tilePath);
    }


    private static Foc getTileCacheDirectory(SolidTileCacheDirectory sdirectory) {
        return sdirectory.getValueAsFile();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////




    ///////////////////////////////////////////////////////////////////////////////////////
    public static final String PRESET_PREFIX = "activity";
    public static final String PREVIEW_EXTENSION = ".preview";

    static public Foc getTrackListDirectory(SolidDataDirectory sdirectory, int i) {
        return getDataDirectory(sdirectory,getPresetPrefix(i));
    }
    static private String getPresetPrefix(int i) {
        return PRESET_PREFIX + i;
    }
    ////////////////////////////////////////////////////////////////////////////////////////




    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final int MAX_TRY=99;
    public static final String GPX_EXTENSION=".gpx";


    public static Foc generateUniqueFilePath(Foc directory, String prefix, String extension)
            throws IOException {

        Foc file = directory.child(generateFileName(prefix, extension));

        int x=1;

        while (file.exists() && x < MAX_TRY) {
            file = directory.child(generateFileName(prefix, x, extension));
            x++;
        }

        if (file.exists()) throw new IOException();

        return file;
    }


    public static String generateDatePrefix() {
        long time = System.currentTimeMillis();
        return String.format(Locale.ROOT,
                "%tY_%tm_%td_%tH_%tM", time, time, time, time, time);
    }


    private static String generateFileName(String prefix, String extension) {
        return prefix + extension;
    }

    private static String generateFileName(String prefix, int i, String extension) {
        return String.format(Locale.ROOT,
                "%s_%d%s", prefix, i, extension);
    }



    public static String parsePrefix(Foc file) {
        StringBuilder name = new StringBuilder(file.getName());

        int length=name.length();

        for (int i=length-1; i>0; i--) {
            if (name.charAt(i)=='.') {
                length=i;
                break;
            }
        }

        name.setLength(length);

        return name.toString();
    }

}
