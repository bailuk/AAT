package ch.bailu.aat.gpx.parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.util_java.parser.OnParsedInterface;

public abstract class AbsXmlParser implements Closeable, GpxPointInterface {
       public abstract void setOnRouteParsed(OnParsedInterface route);
        public abstract void setOnTrackParsed(OnParsedInterface track);
        public abstract void setOnWayParsed(OnParsedInterface way);
        public abstract void parse() throws XmlPullParserException, IOException;
}
