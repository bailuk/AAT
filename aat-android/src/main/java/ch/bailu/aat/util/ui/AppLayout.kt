package ch.bailu.aat.util.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout

object AppLayout {
    private const val BIG_BUTTON_SIZE = 100
    const val DEFAULT_VISIBLE_BUTTON_COUNT = 4
    private const val GPS_EXTRA_BUTTON_COUNT = DEFAULT_VISIBLE_BUTTON_COUNT + 1
    private const val BACK_EXTRA_BUTTON_COUNT = GPS_EXTRA_BUTTON_COUNT + 1
    private const val TABLET_BUTTON_COUNT = BACK_EXTRA_BUTTON_COUNT
    private val size = Point()

    private fun updateMeasurement(context: Context) {
        if (size.x == 0 || size.y == 0) {
            val wm = context.getSystemService(Context.WINDOW_SERVICE)
            if (wm is WindowManager) {
                val display = wm.defaultDisplay
                if (display is Display) {
                    getSizeSDK13(display, size)
                }
            }
        }
    }

    private fun getSizeSDK13(display: Display, size: Point) {
        display.getSize(size)
    }

    private fun getScreenSmallSide(context: Context): Int {
        updateMeasurement(context)
        return Math.min(size.x, size.y)
    }


    fun getOrientation(c: Context): Int {
        return c.resources.configuration.orientation
    }

    fun getOrientationAlongSmallSide(context: Context): Int {
        return if (getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
    }

    fun getOrientationAlongLargeSide(context: Context): Int {
        return if (getOrientationAlongSmallSide(context) == LinearLayout.VERTICAL) LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
    }

    @JvmStatic
    fun getBigButtonSize(context: Context): Int {
        return getBigButtonSize(context, DEFAULT_VISIBLE_BUTTON_COUNT)
    }

    @JvmStatic
    fun getBigButtonSize(context: Context, buttonCount: Int): Int {
        val bigButtonSize = AndroidAppDensity(context).toPixel_i(BIG_BUTTON_SIZE.toFloat())
        return Math.min(getScreenSmallSide(context) / buttonCount, bigButtonSize)
    }

    fun haveExtraSpaceGps(context: Context): Boolean {
        return getVisibleButtonCount(context) >= GPS_EXTRA_BUTTON_COUNT
    }

    private fun getVisibleButtonCount(context: Context): Int {
        val screenSize = getScreenSmallSide(context)
        val buttonSize = getBigButtonSize(context)
        return screenSize / buttonSize
    }

    fun isTablet(context: Context): Boolean {
        return getVisibleButtonCount(context) >= TABLET_BUTTON_COUNT
    }

    @JvmStatic
    fun fadeOut(v: View) {
        fade(v, View.GONE, 1.0f, 0.0f)
    }

    @JvmStatic
    fun fadeIn(v: View) {
        fade(v, View.VISIBLE, 0.0f, 1.0f)
    }

    private fun fade(view: View, visibility: Int, startAlpha: Float, endAlpha: Float) {
        // Taken from org.mapsforge.map.android.input.MapZoomControls
        val anim = AlphaAnimation(startAlpha, endAlpha)
        anim.duration = 250
        view.startAnimation(anim)
        view.visibility = visibility
    }
}
