package ch.bailu.aat.util.fs.foc;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

import java.io.File;
import java.util.List;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;
import ch.bailu.simpleio.foc.FocFile;

public class FocAndroid {
    public static Foc factory(Context c, String id) {
        return factory(c, Uri.parse(id));
    }

    public static Foc factory(Context c, Uri uri) {
        AppLog.d(uri, "factory()");
        AppLog.d(uri, uri.toString());

        String scheme = uri.getScheme();

        if (scheme == null || scheme.equals("file")) {
            return new FocFile(new File(uri.getPath()));

        } else if (scheme.equals("content"))  {
            List<String> segments = uri.getPathSegments();

            if (segments.size() >= 1) {
                if (segments.get(0).equals(FocContent.TREE)) {
                    return new FocContent(c.getContentResolver(), uri, FocContent.TREE);
                } else if (segments.get(0).equals(FocContent.DOCUMENT)) {
                    return new FocContent(c.getContentResolver(), uri, FocContent.DOCUMENT);
                }

            }
        }

        return new FocUri(c.getContentResolver(), uri);
    }
}
