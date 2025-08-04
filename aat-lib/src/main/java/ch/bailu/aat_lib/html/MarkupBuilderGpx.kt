package ch.bailu.aat_lib.html

import ch.bailu.aat_lib.description.AltitudeDescription
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.SpeedDescription
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.preferences.StorageInterface


class MarkupBuilderGpx @JvmOverloads constructor(
    storage: StorageInterface,
    config: MarkupConfig = MarkupConfig.HTML
) : MarkupBuilder(
    config
) {
    private val distance = DistanceDescription(storage)
    private val speed: SpeedDescription = CurrentSpeedDescription(storage)
    private val altitude = AltitudeDescription(storage)

    fun appendInfo(info: GpxInformation, index: Int) {
        val count = index + 1
        val total = info.getGpxList().pointList.size()

        appendHeader(info.getFile().name)
        appendBold(count.toString())
        append("/")
        append(total.toString())
        appendNl()
    }

    fun appendNode(n: GpxPointNode, i: GpxInformation) {
        if (i.getType() == GpxType.TRACK && n.getTimeStamp() != 0L) {
            append(speed.getLabel(), speed.getSpeedDescription(n.getSpeed()))
            appendNl()
        }
        append(altitude.getLabel(), altitude.getAltitudeDescription(n.getAltitude()))
        appendNl()
    }

    fun appendNl(d: ContentDescription) {
        append(d.getLabel(), d.getValueAsString())
        appendNl()
    }

    fun appendAttributes(a: GpxAttributes) {
        if (a.size() > 0) {
            for (i in 0 until a.size()) {
                val kString = a.getSKeyAt(i)
                val v = a.getAt(i)

                if (kString.lowercase().contains("name")) {
                    appendBold(kString, v)
                    appendNl()
                } else {
                    appendKeyValue(kString, v)
                    appendNl()
                }
            }
        }
    }
}
