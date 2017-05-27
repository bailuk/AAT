package ch.bailu.aat.util.fs;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.preferences.SolidDataDirectory;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.simpleio.foc.Foc;

public class AppDirectory  {


    //////////////////////////////////////////////////////////////////////////////////////
/*    public static File getDataDirectory(Context c, File sub) {
        return getDataDirectory(c, sub.toString());
    }*/


    public static Foc getDataDirectory(Context c, String sub) {
        final Foc p = FocAndroid.factory(c, new SolidDataDirectory(c).getValueAsString()).child(sub);
        p.mkdirs();
        return p;
    }

    public static final String DIR_AAT_DATA = "aat_data/";

    public static final String DIR_TILES = "tiles/";
    public static final String DIR_TILES_OSMDROID = "osmdroid/tiles/";

    
    public static final String DIR_LOG = "log/";
    public static final String FILE_LOG = "log.gpx";
    public static Foc getLogFile(Context context) {
        return getDataDirectory(context, DIR_LOG).child(FILE_LOG);
    }
    
    
    public static final String DIR_OVERLAY = "overlay/";
    public static final String DIR_IMPORT = "import/";
    
    public static final String DIR_NOMINATIM = "nominatim/";

    public static final String DIR_OVERPASS = "overpass/";

    
    public static final String DIR_TEST = "test/";
    
    
    

    
    public static final String DIR_CACHE = "cache/";
    public static final String FILE_CACHE_DB="summary.db";
    
    public static Foc getCacheDb(Foc pendingDirectory) {
        final Foc p = pendingDirectory.child(DIR_CACHE);
        p.mkdirs();
        return p.child(FILE_CACHE_DB);
    }
    

    public static final String DIR_EDIT = "overlay/draft";
    public static final String FILE_DRAFT= "draft.gpx";

    public static Foc getEditorDraft(Context c) {
        return getDataDirectory(c, DIR_EDIT).child(FILE_DRAFT);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    
    

    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public static Foc getTileFile(String tilePath, Context context) {
        return getTileCacheDirectory(context).child(tilePath);
    }


    private static Foc getTileCacheDirectory(Context c) {
        final Foc p = new SolidTileCacheDirectory(c).getValueAsFile();
        p.mkdirs();
        return p;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    

    
    ///////////////////////////////////////////////////////////////////////////////////////
    private static final String PRESET_PREFIX = "activity";
    private static final String PREVIEW_EXTENSION = ".preview";

    static public Foc getTrackListDirectory(Context c, int i) {
        return getDataDirectory(c,getPresetPrefix(i));
    }
    static private String getPresetPrefix(int i) {
        return PRESET_PREFIX + i+ "/";
    }
    
    
    public static Foc getTrackListCacheDb(Context c, int i) {
        return getDataDirectory(c, getPresetPrefix(i)+ DIR_CACHE).child(FILE_CACHE_DB);
    }


    static public Foc getPreviewFile(Foc path)  {
        String name = path.getName();
        Foc directory = path.parent();

        Foc cdir = directory.child(DIR_CACHE);
        cdir.mkdirs();
        return cdir.child(name+ PREVIEW_EXTENSION);
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
        } while (file.isReachable() && x < 999);

        if (file.isReachable()) throw new IOException();

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



    public static String parsePrefix(File file) {
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

    
    public static void copyFile(File source, File target) throws IOException {
        if (source.exists()) {
            BufferedInputStream   reader = new BufferedInputStream(new FileInputStream(source));
            BufferedOutputStream  writer = new BufferedOutputStream(new FileOutputStream(target, false));
            
            try {
                int oneByte;
                while ( (oneByte= reader.read()) != -1) {
                    writer.write( oneByte );
                }
                
            } catch( IOException ex ) {
                throw new IOException("ERROR while copying " + source.getPath() + " to " + target.getPath());
            } finally {
                reader.close();
                writer.close();
            }
            
        } else {
            throw new IOException("ERROR: " + source.getPath() + " does not exist.");
        }
    }




}
