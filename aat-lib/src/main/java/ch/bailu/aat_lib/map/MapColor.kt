package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.app.AppColor
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.lib.color.ARGB
import ch.bailu.aat_lib.lib.color.HSV

object MapColor {
    private const val ALPHA_HIGH = 50
    const val ALPHA_LOW = 200
    val LIGHT = setAlpha(ARGB.WHITE, ALPHA_LOW)
    val MEDIUM = setAlpha(ARGB.BLACK, ALPHA_HIGH)
    val DARK = setAlpha(ARGB.BLACK, ALPHA_LOW)

    @JvmField
    val NODE_NEUTRAL = setAlpha(ARGB.LTGRAY, ALPHA_LOW)
    @JvmField
    val NODE_SELECTED = LIGHT

    const val GRID = ARGB.GRAY
    const val EDGE = ARGB.BLACK
    const val TEXT = ARGB.BLACK

    fun setAlpha(color: Int, alpha: Int): Int {
        return ARGB(alpha, ARGB.red(color), ARGB.green(color), ARGB.blue(color)).toInt()
    }

    fun setValue(color: Int, value: Float): Int {
        val hsv = HSV(ARGB(color))
        hsv.setValue(value)
        return hsv.toInt()
    }

    private fun setSaturation(color: Int, saturation: Float): Int {
        val hsv = HSV(ARGB(color))
        hsv.setSaturation(saturation)
        return hsv.toInt()
    }

    fun getColorFromIID(iid: Int): Int {
        return overlayColorMap[iid]?: AppColor.HL_ORANGE
    }

    private val overlayColorMap = HashMap<Int, Int>().apply {
        put(InfoID.TRACKER, AppColor.HL_ORANGE)
        for (i in 0 until AppColor.OVERLAY_COLOR.size) {
            put(InfoID.OVERLAY + i, AppColor.OVERLAY_COLOR[i])
        }
        put(InfoID.EDITOR_DRAFT, AppColor.HL_GREEN)
        put(InfoID.EDITOR_OVERLAY, ARGB.MAGENTA)
        put(InfoID.FILE_VIEW, AppColor.HL_ORANGE)
        put(InfoID.NOMINATIM_REVERSE, AppColor.HL_OVERLAY_COLOR[0])
        put(InfoID.NOMINATIM, AppColor.HL_OVERLAY_COLOR[1])
        put(InfoID.OVERPASS, AppColor.HL_OVERLAY_COLOR[2])
        put(InfoID.POI, AppColor.HL_OVERLAY_COLOR[2])
        put(InfoID.BROUTER, AppColor.HL_OVERLAY_COLOR[3])
    }

    fun toDarkTransparent(colorIn: Int): Int {
        var colorOut = colorIn
        colorOut = setValue(colorOut, 0.30f)
        colorOut = setAlpha(colorOut, ALPHA_LOW)
        return colorOut
    }

    fun toLightTransparent(colorIn: Int): Int {
        var colorOut = colorIn
        colorOut = setSaturation(colorOut, 0.25f)
        colorOut = setAlpha(colorOut, ALPHA_LOW)
        return colorOut
    }
}
