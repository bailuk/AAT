package ch.bailu.aat.util.fs.foc;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.List;

import ch.bailu.simpleio.foc.Foc;
import ch.bailu.simpleio.foc.FocFile;
import ch.bailu.simpleio.foc.FocName;

public class FocAndroid {
    public static Foc factory(Context c, String id) {
        return factory(c, Uri.parse(id));
    }

    public static Foc factory(Context c, Uri uri) {
        String scheme = uri.getScheme();

        Foc result = factoryFocName(scheme, uri);

        if (result == null) {
            result = factoryFocFile(scheme, uri);
        }

        if (result == null) {
            result = factoryFocContent(scheme, uri, c.getContentResolver());
        }

        if (result == null) {
            result = new FocUri(c.getContentResolver(), uri);
        }

        return result;
    }

    private static Foc factoryFocName(String scheme, Uri uri) {
        String name = uri.toString();


        if (scheme == null && (name.length() == 0 || name.charAt(0) != '/')) {
            return new FocName(name);
        }

        return null;
    }


    private static Foc factoryFocFile(String scheme, Uri uri) {
        if (scheme == null || "file".equals(scheme)) {
            return new FocFile(new File(uri.getPath()));
        }

        return null;
    }

    // [content]://[authority]/[uri type]/[document ID]/[document type]/[document ID]
    private static Foc factoryFocContent(String scheme, Uri uri, ContentResolver r) {
        if ("content".equals(scheme) &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            List<String> segments = uri.getPathSegments();


            if (segments.size() == 2) {
                Uri permission = uri;
                DocumentId documentId = new DocumentId(Uri.decode(segments.get(1)));

                if (FocContent.TREE.equals(segments.get(0)))
                    return new FocContent(r, permission, documentId, FocContent.TREE);
                else if (FocContent.DOCUMENT.equals(segments.get(0)))
                    return new FocContent(r, permission, documentId, FocContent.DOCUMENT);

            } else if (segments.size() == 4) {
                Uri permission =
                        new Uri.Builder().
                                scheme(scheme).
                                authority(uri.getAuthority()).
                                appendPath(segments.get(0)).
                                appendPath(segments.get(1)).
                                build();

                DocumentId documentId = new DocumentId(Uri.decode(segments.get(3)));


                Foc foc =  new FocContent(r, permission, documentId, FocContent.UNKNOWN);

                /*
                AppLog.d(foc, foc.getName());
                AppLog.d(foc, foc.getPathName());
                AppLog.d(foc, foc.getPath());
                AppLog.d(foc, permission.toString());
                */

                return foc;
            }

        }
        return null;
    }

}
