package ch.bailu.aat_lib.util

/**
 * Simple rectangle util class.
 * To keep some code independent from UI libraries.
 */
class Rect {
    @JvmField
    var left: Int = 0
    @JvmField
    var right: Int = 0
    @JvmField
    var top: Int = 0
    @JvmField
    var bottom: Int = 0

    val width
        get() =  right - left + 1

    val height
        get() = bottom - top + 1


    /**
     * Set new left and top coordinates while keeping width and height
     * @param x new left
     * @param y new top
     */
    fun offsetTo(x: Int, y: Int) {
        val width = right - left
        val height = bottom - top

        left = x
        right = x + width
        top = y
        bottom = y + height
    }

    /**
     * Add offset to left and top coordinates while keeping width and height
     * @param x add to left
     * @param y add to top
     */
    fun offset(x: Int, y: Int) {
        left += x
        right += x
        top += y
        bottom += y
    }

    override fun toString(): String {
        return "Rect{" +
                "left=" + left +
                ", right=" + right +
                ", top=" + top +
                ", bottom=" + bottom +
                ", width=" + width +
                ", height=" + height +
                '}'
    }
}
