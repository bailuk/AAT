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
    var exception: Exception? = null
        private set

    private var haveNewSegment = true

    init {
        try {
            inputFile.openR().use {
                val jsonContent = it.readBytes().toString(Charsets.UTF_8)
                val jsonMap = Json.parse(jsonContent)
                val time = inputFile.lastModified()

                parseValhalla(jsonMap, time)
                parseGraphHopper(jsonMap, time)
                parseOSRM(jsonMap, time)
            }
        } catch (e: Exception) {
            exception = e
        }
    }

    private fun parseOSRM(jsonMap: JsonMap, time: Long) {
        var time = time
        jsonMap.map("routes") { routes ->
            haveNewSegment = true
            routes.map("legs") { legs ->
                legs.map("steps") { steps ->
                    steps.number("duration") { duration ->
                        time += (duration * 1000.0).toLong()
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

    private fun parseGraphHopper(jsonMap: JsonMap, time: Long) {
        jsonMap.map("paths") { paths ->
            haveNewSegment = true
            paths.string("points") { points ->
                PolylineDecoder(points) { la, lo ->
                    append(LatLongE6(la / 1e5, lo / 1e5), time)
                }
            }
        }
    }

    private fun parseValhalla(jsonMap: JsonMap, time: Long) {
        jsonMap.map("trip") { trip ->
            haveNewSegment = true
            trip.map("legs") { legs ->
                legs.string("shape") { shape ->
                    PolylineDecoder(shape) { la, lo ->
                        append(LatLongE6(la, lo), time)
                    }
                }
            }
        }
    }

    private fun append(latLong: LatLongInterface, time: Long, gpxAttributes: GpxAttributes = GpxAttributesNull()) {
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
