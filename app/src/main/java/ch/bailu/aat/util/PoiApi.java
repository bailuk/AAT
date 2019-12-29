package ch.bailu.aat.util;

import android.content.Context;

import java.io.UnsupportedEncodingException;

import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;

public class PoiApi extends OsmApiHelper {
    public final static String NAME="MapsForge Poi";
    public final static String EXT=".gpx";

    private final Foc directory;

    public PoiApi(Context context) {
        directory = AppDirectory.getDataDirectory(context, AppDirectory.DIR_NOMINATIM);

    }
    @Override
    public String getApiName() {
        return NAME;
    }

    @Override
    public String getUrl(String query) throws UnsupportedEncodingException {
        return "";
    }

    @Override
    public String getUrlStart() {
        return "";
    }

    @Override
    public Foc getBaseDirectory() {
        return directory;
    }

    @Override
    public String getFileExtension() {
        return EXT;
    }

    @Override
    public String getUrlPreview(String s) {
        return "";
    }
}
