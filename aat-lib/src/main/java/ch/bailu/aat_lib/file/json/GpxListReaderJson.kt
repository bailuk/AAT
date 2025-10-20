package ch.bailu.aat_lib.file.json

import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesStatic
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.attributes.Keys
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
                parseGeocoding(jsonMap, time)

                if (gpxList.pointList.size() == 0) {
                    val jsonRootMap = Json.parse("{ \"root\": $jsonContent }")
                    parseCM(jsonRootMap)
                }
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

    private fun parseCM(jsonMap: JsonMap) {
        val keyIndex = Keys.toIndex("device")

        jsonMap.map("root") {
            var latitude = 0
            var longitude = 0
            var timestamp = 0L
            val attributes = GpxAttributesStatic()
            var countAttributes = 0

            it.string("device") {
                countAttributes++
                attributes.put(keyIndex, it.take(10))
            }
            it.number("latitude") {
                countAttributes++
                latitude = it.toInt()
            }
            it.number("longitude") {
                countAttributes++
                longitude = it.toInt()
            }

            it.number("timestamp") {
                countAttributes++
                timestamp = it.toLong() * 1000
            }

            if (countAttributes == 4) {
                append(LatLongE6(latitude, longitude), timestamp, attributes)
                gpxList.setType(GpxType.WAY)
            }
        }
    }

    /**
     * https://nominatim.openstreetmap.org/reverse?format=geocodejson&lat=47.375006&lon=8.530892&zoom=15
     * https://nominatim.org/release-docs/develop/api/Reverse/#output-format
     */
    private fun parseGeocoding(jsonMap: JsonMap, timestamp: Long) {
        jsonMap.map("features") {
            var latitude = 0.0
            var longitude = 0.0
            val attributes = GpxAttributesStatic()
            var countAttributes = 0

            it.map("properties") {
                it.string("geocoding") { k, v ->
                    attributes.put(Keys.toIndex(k), v)
                }
                it.map("geocoding") {
                    it. string("admin") { k, v ->
                        attributes.put(Keys.toIndex("admin.$k"), v)
                    }
                }
            }
            it.map("geometry") {
                it.number("coordinates") {
                    if (countAttributes++ == 0) longitude = it
                    else latitude = it
                }
            }

            if (countAttributes == 2) {
                append(LatLongE6(latitude, longitude), timestamp, attributes)
                gpxList.setType(GpxType.WAY)
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
