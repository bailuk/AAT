package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes.Companion.factoryRoute
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes.Companion.factoryTrack
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.service.background.ThreadControl
import ch.bailu.aat_lib.xml.parser.BOM
import ch.bailu.aat_lib.xml.parser.XmlParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import ch.bailu.aat_lib.xml.parser.util.OnParsedInterface
import ch.bailu.foc.Foc
import java.io.IOException

class GpxListReader private constructor(
    private val threadControl: ThreadControl,
    inputFile: Foc,
    trackAttributes: GpxListAttributes
) {
    private val way: OnParsed = OnParsed(GpxType.WAY, GpxListAttributes.NULL)
    private val track: OnParsed = OnParsed(GpxType.TRACK, trackAttributes)
    private val route: OnParsed = OnParsed(GpxType.ROUTE, factoryRoute())

    private var parsedPointAccess: GpxPointInterface = GpxPoint.NULL

    var exception: Exception? = null
        private set

    constructor(inputFile: Foc, autoPause: AutoPause) : this(ThreadControl.KEEP_ON, inputFile, autoPause)
    constructor(c: ThreadControl, inputFile: Foc, autoPause: AutoPause) : this(c, inputFile, factoryTrack(autoPause))

    init {
        try {
            val parser = XmlParser(inputFile)
            parsedPointAccess = parser

            parser.setOnRouteParsed(route)
            parser.setOnTrackParsed(track)
            parser.setOnWayParsed(way)
            parser.parse()
            parser.close()
        } catch (e: Exception) {
            exception = e
        }
    }

    val gpxList: GpxList
        get() {
            if (track.hasContent()) return track.gpxList
            return if (route.hasContent()) route.gpxList else way.gpxList
        }

    private inner class OnParsed(type: GpxType, attr: GpxListAttributes) : OnParsedInterface {
        val gpxList: GpxList = GpxList(type, attr)
        private var haveNewSegment = true

        fun hasContent(): Boolean {
            return gpxList.pointList.size() > 0
        }

        override fun onHaveSegment() {
            haveNewSegment = true
        }

        @Throws(IOException::class)
        override fun onHavePoint() {
            if (threadControl.canContinue()) {
                if (haveNewSegment) {
                    gpxList.appendToNewSegment(
                        GpxPoint(parsedPointAccess),
                        parsedPointAccess.getAttributes()
                    )
                    haveNewSegment = false
                } else {
                    gpxList.appendToCurrentSegment(
                        GpxPoint(parsedPointAccess),
                        parsedPointAccess.getAttributes()
                    )
                }
            } else {
                throw IOException()
            }
        }
    }
}
