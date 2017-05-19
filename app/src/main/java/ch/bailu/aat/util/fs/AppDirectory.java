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

public class AppDirectory  {


    //////////////////////////////////////////////////////////////////////////////////////
/*    public static File getDataDirectory(Context c, File sub) {
        return getDataDirectory(c, sub.toString());
    }*/


    public static File getDataDirectory(Context c, String sub) {
        final File p = new File(new SolidDataDirectory(c).getValueAsString(), sub);
        p.mkdirs();
        return p;
    }

    public static final String DIR_AAT_DATA = "aat_data/";

    public static final String DIR_TILES = "tiles/";
    public static final String DIR_TILES_OSMDROID = "osmdroid/tiles/";

    
    public static final String DIR_LOG = "log/";
    public static final String FILE_LOG = "log.gpx";
    public static File getLogFile(Context context) {
        return new File(getDataDirectory(context, DIR_LOG), FILE_LOG);
    }
    
    
    public static final String DIR_OVERLAY = "overlay/";
    public static final String DIR_IMPORT = "import/";
    
    public static final String DIR_NOMINATIM = "nominatim/";

    public static final String DIR_OVERPASS = "overpass/";

    
    public static final String DIR_TEST = "test/";
    
    
    

    
    public static final String DIR_CACHE = "cache/";
    public static final String FILE_CACHE_DB="summary.db";
    
    public static File getCacheDb(File pendingDirectory) {
        final File p = new File(pendingDirectory, DIR_CACHE);
        p.mkdirs();
        return new File(p, FILE_CACHE_DB);
    }
    

    public static final String DIR_EDIT = "overlay/draft";
    public static final String FILE_DRAFT= "draft.gpx";

    public static File getEditorDraft(Context c) {
        return new File(AppDirectory.getDataDirectory(c, DIR_EDIT), FILE_DRAFT);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    
    

    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public static File getTileFile(String tilePath, Context context) {
        return new File(getTileCacheDirectory(context),tilePath);
    }


    private static File getTileCacheDirectory(Context c) {
        final File p = new SolidTileCacheDirectory(c).getValueAsFile();
        p.mkdirs();
        return p;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    

    
    ///////////////////////////////////////////////////////////////////////////////////////
    private static final String PRESET_PREFIX = "activity";
    private static final String PREVIEW_EXTENSION = ".preview";

    static public File getTrackListDirectory(Context c, int i) {
        return getDataDirectory(c,getPresetPrefix(i));
    }
    static private String getPresetPrefix(int i) {
        return PRESET_PREFIX + i+ "/";
    }
    
    
    public static File getTrackListCacheDb(Context c, int i) {
        return new File(getDataDirectory(c, getPresetPrefix(i)+ DIR_CACHE), FILE_CACHE_DB);
    }


    static public File getPreviewFile(File path)  {
        String name = path.getName();
        String directory = path.getParent();


        File cdir = new File(directory, DIR_CACHE);
        cdir.mkdirs();
        return new File(cdir, name+ PREVIEW_EXTENSION);
    }
    
    

    ////////////////////////////////////////////////////////////////////////////////////////


    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final String GPX_EXTENSION=".gpx";
    public static final String OSM_EXTENSION=".osm";

    public static File generateUniqueFilePath(File directory, String prefix, String extension) 
            throws IOException {
        File file;

        int x=0;
        do {
            file = new File(directory, generateFileName(prefix, x, extension));
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
