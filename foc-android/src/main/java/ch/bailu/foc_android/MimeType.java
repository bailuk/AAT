package ch.bailu.foc_android;

public class MimeType {

    public static String fromName(String name) {
        String suffix = getSuffix(name);

        if ("gpx".equals(suffix)) {
            return "application/gpx+xml";

        } else if ("osm".equals(suffix) || "xml".equals(suffix)) {
            return "application/xml";

        } else if ("png".equals(suffix)) {
            return "image/png";

        } else if ("zip".equals(suffix)) {
            return "application/zip";
        }
        return "text/plain";
    }


    private static String getSuffix(String name) {
        String ext = "";

        int last = name.lastIndexOf('.');

        if (last > -1) {
            last ++;
            if (last < name.length())
                ext =  name.substring(last);
        }
        return ext;
    }
}
