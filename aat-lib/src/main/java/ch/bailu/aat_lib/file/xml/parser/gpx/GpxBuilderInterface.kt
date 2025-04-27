package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.file.xml.parser.util.OnParsedInterface
import org.xmlpull.v1.XmlPullParserException
import java.io.Closeable
import java.io.IOException

/**
 * Interface to build GPX Tracks
 */
interface GpxBuilderInterface : Closeable, GpxPointInterface {
    fun setOnRouteParsed(route: OnParsedInterface)
    fun setOnTrackParsed(track: OnParsedInterface)
    fun setOnWayParsed(way: OnParsedInterface)

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse()
}
