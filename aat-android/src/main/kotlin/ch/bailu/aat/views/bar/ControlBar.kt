package ch.bailu.aat.views.bar

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.AppLayout.getBigButtonSize
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.preferences.SolidImageButton
import ch.bailu.aat_lib.preferences.SolidIndexList

open class ControlBar(context: Context, private val _orientation: Int, val theme: UiTheme, visibleButtonCount: Int = AppLayout.DEFAULT_VISIBLE_BUTTON_COUNT) :
    LinearLayout(context) {
    private val canvas = LinearLayout(context)
    val controlSize = getBigButtonSize(context, visibleButtonCount)

    private val onClickListeners = ArrayList<OnClickListener>()

    private val onClickListener = OnClickListener { v ->
        onClickListeners.forEach { it.onClick(v) }
    }

    init {
        theme.background(this)

        canvas.orientation = _orientation
        orientation = _orientation

        val scroller: ViewGroup = if (orientation == HORIZONTAL) {
            HorizontalScrollView(context)
        } else {
            ScrollView(context)
        }

        scroller.addView(canvas)
        super.addView(scroller)
    }

    fun place(x: Int, y: Int, length: Int) {
        val small = controlSize
        val largeSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY)
        val smallSpec = MeasureSpec.makeMeasureSpec(small, MeasureSpec.EXACTLY)
        if (_orientation == HORIZONTAL) {
            measure(largeSpec, smallSpec)
            layout(x, y, x + length, y + small)
        } else {
            measure(smallSpec, largeSpec)
            layout(x, y, x + small, y + length)
        }
    }

    @JvmOverloads
    fun addImageButton(res: Int, size: Int = controlSize): ImageButtonViewGroup {
        val button = ImageButtonViewGroup(context, res)
        button.setOnClickListener(onClickListener)
        theme.button(button)
        add(button, size)
        return button
    }

    fun add(v: View): View {
        add(v, controlSize)
        return v
    }

    fun add(v: View, size: Int): View {
        canvas.addView(v, size, controlSize)
        return v
    }

    override fun addView(v: View) {
        add(v)
    }

    fun addSpace() {
        add(View(context))
    }

    fun addButton(button: View): View {
        canvas.addView(button, controlSize, controlSize)
        button.setOnClickListener(onClickListener)
        return button
    }

    fun addSolidIndexButton(slist: SolidIndexList): View {
        val button: View = SolidImageButton(context, slist)
        theme.button(button)
        return add(button)
    }

    fun addOnClickListener(l: OnClickListener) {
        onClickListeners.add(l)
    }
}
