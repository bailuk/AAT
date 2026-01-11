package ch.bailu.aat_lib.app

/**
 * Application color palette (Accent and theme colors)
 * AAT/aat-android/util/assets/icons/palette.xcf
 *
 * 0xAARRGGBB
 * A = Alpha
 * R = Red
 * G = Green
 * B = Blue
 */
object AppColor {
    const val HL_ORANGE = 0xFFFF6600.toInt()
    const val HL_GREEN = 0xFFCCFF00.toInt()
    const val HL_BLUE = 0xFF00D8FF.toInt()
    const val HL_DARK = 0xFF444563.toInt()

    const val GRAY_MEDIUM = 0XFFD0D0D0.toInt()
    const val GRAY_DARK = 0XFF9D9D9D.toInt()
    const val GRAY_LIGHT = 0XFFEDEDED.toInt()

    val OVERLAY_COLOR = intArrayOf(
        0xFFFF939F.toInt(),
        0xFFFF94fD.toInt(),
        0xFF94FFB7.toInt(),
        0xFFFDFF94.toInt(),
    )

    val HL_OVERLAY_COLOR = intArrayOf(
        0XFFFCFF3F.toInt(),
        0XFF3FFF7D.toInt(),
        0XFFFF3FFC.toInt(),
        0XFFFF3F54.toInt(),
    )
}
