package ch.bailu.aat.util;

import android.content.Context;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.simpleio.foc.Foc;

public class NominatimApi extends OsmApiHelper {
    public final static String NAME="Nominatim";
    public final static String URL="http://nominatim.openstreetmap.org/search/";
    public final static String POST="?format=xml";
    public final static String EXT=".xml";

    
    private final Foc directory;
    private final String bounding;
    
    public NominatimApi(Context context, BoundingBoxE6 boundingBox) {
        directory = AppDirectory.getDataDirectory(context, AppDirectory.DIR_NOMINATIM);
        bounding = toString(boundingBox);
    }
    
    

    private static String toString(BoundingBoxE6 b) {
        if (b.getLatitudeSpanE6()>0 && b.getLongitudeSpanE6() > 0) {
            return 
                    "&bounded=1&viewbox=" + 
                    toS(b.getLonWestE6())  + "," +
                    toS(b.getLatNorthE6()) + "," +
                    toS(b.getLonEastE6())  + "," +
                    toS(b.getLatSouthE6());
        } else {
            return "";
        }
    }



    private static String toS(int i) {
        final double d=i;
        return Double.valueOf(d/1E6).toString();
    }



    

    
    @Override
    public String getApiName() {
        return NAME;
    }

    @Override
    public String getUrl(String query) throws UnsupportedEncodingException {
        final StringBuilder url = new StringBuilder();        
        url.setLength(0);
        url.append(URL);
        url.append(URLEncoder.encode(query, "UTF-8"));
        url.append(URLEncoder.encode(POST, "UTF-8"));
        url.append(bounding);
        return url.toString();
    }

    
    @Override
    public String getUrlEnd() {
        return POST+bounding;
    }

    @Override
    public String getUrlStart() {
        return URL;
    }

    
    @Override
    public Foc getBaseDirectory() {
        return directory;
    }


    @Override
    public String getFileExtension() {
        return EXT;
    }
}
