package ch.bailu.aat_lib.util.net;


import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;


public class URX {

    private URL url;
    private MalformedURLException urlException;

    private String string;


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

}
