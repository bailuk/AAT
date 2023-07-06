package ch.bailu.aat.views.description.mview

import android.view.View
import android.view.ViewGroup
import ch.bailu.aat.util.ui.theme.AppTheme

class MultiViewIndicator(private val multiView: MultiView) : ViewGroup(
    multiView.context
) {
    private val indicatorView: View
    private var width = 0

    init {
        multiView.addObserver { layoutIndicator() }
        setBackgroundColor(0)
        isClickable = false
        indicatorView = View(context)
        indicatorView.setBackgroundColor(THEME.getHighlightColor())
        indicatorView.isClickable = false
        addView(indicatorView)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        width = r - l
        layoutIndicator()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(HEIGHT, MeasureSpec.EXACTLY))
    }

    private fun layoutIndicator() {
        val active = multiView.getActive()
        val count = multiView.pageCount()
        if (count > 0) {
            val w = width / count
            val x = w * active
            indicatorView.layout(x, 0, x + w, HEIGHT)
        }
    }

    companion object {
        private val THEME = AppTheme.bar
        private const val HEIGHT = 5
    }
}
