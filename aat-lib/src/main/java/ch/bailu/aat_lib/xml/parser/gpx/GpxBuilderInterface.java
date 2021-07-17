package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParserException;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.util_java.parser.OnParsedInterface;

/**
 * Interface to build GPX Tracks
 */
public interface GpxBuilderInterface extends Closeable, GpxPointInterface {
    void setOnRouteParsed(OnParsedInterface route);
    void setOnTrackParsed(OnParsedInterface track);
    void setOnWayParsed(OnParsedInterface way);
    void parse() throws XmlPullParserException, IOException;
}
