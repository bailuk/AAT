package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.app.AppColor
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.lib.color.ARGB
import ch.bailu.aat_lib.lib.color.HSV

object MapColor {
    //private static final UiTheme theme = AppTheme.bar;
    private const val ALPHA_HIGH = 50
    const val ALPHA_LOW = 200
    @JvmField
    val LIGHT = setAlpha(ARGB.WHITE, ALPHA_LOW)
    val MEDIUM = setAlpha(ARGB.BLACK, ALPHA_HIGH)
    val DARK = setAlpha(ARGB.BLACK, ALPHA_LOW)
    @JvmField
    val NODE_NEUTRAL = setAlpha(ARGB.LTGRAY, ALPHA_LOW)
    @JvmField
    val NODE_SELECTED = LIGHT
    const val GRID = ARGB.GRAY
    const val EDGE = ARGB.BLACK //AppTheme.getAltBackgroundColor();
    const val TEXT = ARGB.BLACK

    //public static final int LEGEND_TEXT = Color.BLACK;
    @JvmStatic
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

    @JvmStatic
    fun getColorFromIID(iid: Int): Int {
        val overlayCount = AppColor.OVERLAY_COLOR.size
        if (iid == InfoID.TRACKER) return AppColor.HL_ORANGE
        if (iid >= InfoID.OVERLAY && iid < InfoID.OVERLAY + overlayCount) {
            val slot = iid - InfoID.OVERLAY
            return AppColor.OVERLAY_COLOR[slot]
        }
        if (iid == InfoID.EDITOR_DRAFT) return AppColor.HL_GREEN
        if (iid == InfoID.EDITOR_OVERLAY) return ARGB.MAGENTA //AppTheme.getHighlightColor3();
        return if (iid == InfoID.FILE_VIEW) AppColor.HL_BLUE else AppColor.HL_ORANGE
    }

    fun toDarkTransparent(colorIn: Int): Int {
        var colorOut = colorIn
        colorOut = setValue(colorOut, 0.30f)
        colorOut = setAlpha(colorOut, ALPHA_LOW)
        return colorOut
    }

    @JvmStatic
    fun toLightTransparent(colorIn: Int): Int {
        var colorOut = colorIn
        colorOut = setSaturation(colorOut, 0.25f)
        colorOut = setAlpha(colorOut, ALPHA_LOW)
        return colorOut
    }
}
