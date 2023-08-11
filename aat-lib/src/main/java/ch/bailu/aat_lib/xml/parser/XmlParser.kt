package ch.bailu.aat_lib.xml.parser

import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.xml.parser.gpx.GpxBuilderInterface
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import ch.bailu.aat_lib.xml.parser.util.OnParsedInterface
import ch.bailu.foc.Foc
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.Reader

class XmlParser(file: Foc) : GpxBuilderInterface {
    private val scanner: Scanner
    private val reader: Reader
    private val parser: XmlPullParser

    init {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        parser = factory.newPullParser()
        reader = BOM.open(file)
        scanner = Scanner(file.lastModified())
    }

    @Throws(XmlPullParserException::class, IOException::class)
    override fun parse() {
        parser.setInput(reader)
        RootParser().parse(parser, scanner)
    }

    @Throws(IOException::class)
    override fun close() {
        reader.close()
    }

    override fun setOnRouteParsed(route: OnParsedInterface) {
        scanner.routeParsed = route
    }

    override fun setOnTrackParsed(track: OnParsedInterface) {
        scanner.trackParsed = track
    }

    override fun setOnWayParsed(way: OnParsedInterface) {
        scanner.wayParsed = way
    }

    override fun getLatitudeE6(): Int {
        return scanner.latitude.int
    }

    override fun getLongitudeE6(): Int {
        return scanner.longitude.int
    }

    override fun getAltitude(): Double {
        return scanner.altitude.int.toDouble()
    }

    override fun getTimeStamp(): Long {
        return scanner.dateTime.timeMillis
    }

    override fun getAttributes(): GpxAttributes {
        return scanner.tags.get()
    }

    override fun getLongitude(): Double {
        return getLongitudeE6() / 1E6
    }

    override fun getLatitude(): Double {
        return getLatitudeE6() / 1E6
    }
}
