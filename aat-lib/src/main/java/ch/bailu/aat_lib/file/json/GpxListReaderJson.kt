package ch.bailu.aat_lib.file.json

import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.lib.json.decoder.PolylineDecoder
import ch.bailu.aat_lib.lib.json.parser.Json
import ch.bailu.aat_lib.lib.json.parser.JsonMap
import ch.bailu.foc.Foc

class GpxListReaderJson(inputFile: Foc) {
    val gpxList = GpxList(GpxType.ROUTE, GpxListAttributes.factoryRoute())

    private var haveNewSegment = true

    init {
        val jsonContent = inputFile.openR().readBytes().toString(Charsets.UTF_8)
        val jsonMap = Json.parse(jsonContent)

        parseValhalla(jsonMap)
        parseGraphHopper(jsonMap)
        parseOSRM(jsonMap)
    }

    private fun parseOSRM(jsonMap: JsonMap) {
        jsonMap.map("routes") { routes ->
            haveNewSegment = true
            routes.map("legs") { legs ->
                legs.map("steps") { steps ->
                    var time = 0L
                    steps.number("duration") { duration ->
                        time = (duration * 1000.0).toLong()
                    }
                    steps.map("maneuver") { maneuver ->
                        var first = true
                        var la = 0.0
                        var lo = 0.0
                        maneuver.number("location") { location ->
                            if (first) {
                                lo = location
                                first = false
                            } else {
                                la = location
                            }
                        }
                        append(LatLongE6(la, lo), time)
                    }
                }
            }
        }
    }

    private fun parseGraphHopper(jsonMap: JsonMap) {
        jsonMap.map("paths") { paths ->
            haveNewSegment = true
            paths.string("points") { points ->
                PolylineDecoder(points) { la, lo ->
                    append(LatLongE6(la / 1e5, lo / 1e5))
                }
            }
        }
    }

    private fun parseValhalla(jsonMap: JsonMap) {
        jsonMap.map("trip") { trip ->
            haveNewSegment = true
            trip.map("legs") { legs ->
                legs.string("shape") { shape ->
                    PolylineDecoder(shape) { la, lo ->
                        append(LatLongE6(la, lo))
                    }
                }
            }
        }
    }

    private fun append(latLong: LatLongInterface, time: Long = 0, gpxAttributes: GpxAttributes = GpxAttributesNull()) {
        append(GpxPoint(latLong, 0f, time), gpxAttributes)
    }

    private fun append(gpxPoint: GpxPoint, gpxAttributes: GpxAttributes) {
        if (haveNewSegment) {
            gpxList.appendToNewSegment(gpxPoint, gpxAttributes)
            haveNewSegment = false
        } else {
            gpxList.appendToCurrentSegment(gpxPoint, gpxAttributes)
        }
    }
}
