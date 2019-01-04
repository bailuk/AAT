package ch.bailu.aat.gpx.new_parser.parser;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;


public abstract class Attr {
    public Attr(XmlPullParser p) throws IOException {

        for (int i=0; i<p.getAttributeCount(); i++) {
            if (p.getAttributeName(i) != null && p.getAttributeValue(i) != null) {
                attribute(p.getAttributeName(i), p.getAttributeValue(i));
            }
        }
    }

    public abstract void attribute(String name, String value) throws IOException;
}
