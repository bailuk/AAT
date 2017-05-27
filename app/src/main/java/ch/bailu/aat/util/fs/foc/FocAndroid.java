package ch.bailu.aat.util.fs.foc;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import ch.bailu.simpleio.foc.Foc;
import ch.bailu.simpleio.foc.FocFile;

public class FocAndroid {
    public static Foc factory(Context c, String id) {
        if (id.length()>0 && id.charAt(0) == '/') {
            return new FocFile(new File(id));
        } else {
            return new FocContent(c.getContentResolver(), Uri.parse(id));
        }
    }
}
