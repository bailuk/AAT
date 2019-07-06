package ch.bailu.aat.util.net;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;


public class URX {
    private Uri uri;

    private URL url;
    private MalformedURLException urlException;

    private String string;


    public URX(URL u) {
        url = u;
    }
    public URX(String s) {
        string = s;
    }
    public URX(Uri u) {
        uri = u;
    }



    @NonNull
    @Override
    public String toString() {
        if (string == null) {
            if (uri != null) {
                string = uri.toString();
            } else if (url != null) {
                string = url.toString();
            }
        }

        if (string == null) string = "";

        return string;
    }


    public URL getURL() {
        try {
            return toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }



    public URL toURL() throws MalformedURLException {
        if (urlException != null) throw urlException;


        if (url == null) {
            url = new URL(toString());
        }

        return url;
    }

    public Uri toUri() {
        if (uri == null) {
            uri = Uri.parse(toString());
        }
        return uri;
    }
}
