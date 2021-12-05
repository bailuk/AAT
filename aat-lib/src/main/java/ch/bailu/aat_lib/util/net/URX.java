package ch.bailu.aat_lib.util.net;


import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;


public class URX {

    private URL url = null;
    private String string = null;

    private MalformedURLException urlException = null;

    public URX(URL u) {
        url = u;
    }
    public URX(String s) {
        string = s;
    }


    @Nonnull
    @Override
    public String toString() {
        if (string == null) {
            string = url.toString();
        }

        if (string == null) {
            string = "";
        }

        return string;
    }

    public URL toURL() throws MalformedURLException {
        if (urlException != null) {
            throw urlException;
        }

        if (url == null) {
            try {
                url = new URL(toString());
            } catch (MalformedURLException e) {
                urlException = e;
                throw urlException;
            }
        }

        return url;
    }
}
