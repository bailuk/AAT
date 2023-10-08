package ch.bailu.aat.views.description.mview

import android.view.KeyEvent
import ch.bailu.aat.R
import ch.bailu.aat.activities.AbsHardwareButtons
import ch.bailu.aat.activities.AbsHardwareButtons.OnHardwareButtonPressed
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.image.ImageButtonViewGroup


class MultiViewNextButton(private val mv: MultiView, theme: UiTheme) : ImageButtonViewGroup(
    mv.context, R.drawable.go_next_inverse
), OnHardwareButtonPressed {
    init {
        setOnClickListener { mv.setNext() }
        theme.button(this)
    }

    override fun onHardwareButtonPressed(
        code: Int,
         type: AbsHardwareButtons.EventType
    ): Boolean {
        if (code == KeyEvent.KEYCODE_SEARCH) {
            if (type === AbsHardwareButtons.EventType.DOWN) mv.setNext()
            return true
        }
        return false
    }
}
