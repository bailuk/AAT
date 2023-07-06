package ch.bailu.aat.views.msg.overlay

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import ch.bailu.aat.views.msg.AbsMsgView

class MessageOverlay(context: Context) : LinearLayout(context) {
    private val messageViews = ArrayList<AbsMsgView>(5)

    init {
        orientation = VERTICAL
    }

    fun addSpace() {
        addSpace(this)
    }

    fun add(v: AbsMsgView): AbsMsgView {
        add(this, v)
        return v
    }

    fun addR(v: AbsMsgView): AbsMsgView {
        val wrapper = LinearLayout(context)
        wrapper.orientation = HORIZONTAL
        wrapper.setBackgroundColor(Color.TRANSPARENT)
        addSpace(wrapper)
        add(wrapper, v)
        addView(
            wrapper,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        )
        return v
    }

    private fun add(parent: ViewGroup, view: AbsMsgView) {
        parent.addView(
            view,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        )
        messageViews.add(view)
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        for (v in messageViews) {
            v.attach()
        }
    }

    public override fun onDetachedFromWindow() {
        for (v in messageViews) {
            v.detach()
        }
        super.onDetachedFromWindow()
    }

    companion object {
        private fun addSpace(v: LinearLayout) {
            val space = LinearLayout(v.context)
            space.orientation = v.orientation
            space.setBackgroundColor(Color.TRANSPARENT)
            v.addView(space, LayoutParams(0, 0, 1f))
        }
    }
}
