package ch.bailu.aat.views.description.mview

import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat.menus.MultiViewMenu
import ch.bailu.aat.util.ui.theme.AppTheme

class MultiViewSelector(private val multiView: MultiView) : LinearLayout(
    multiView.context
) {
    private val label: TextView = TextView(multiView.context)

    init {
        label.text = multiView.label
        label.setSingleLine()
        AppTheme.bar.header(label)
        AppTheme.bar.button(this)
        addView(label)
        gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        orientation = HORIZONTAL
        setOnClickListener {
            MultiViewMenu(multiView).showAsPopup(
                context,
                this@MultiViewSelector
            )
        }
        multiView.addObserver { label.text = multiView.label }
    }
}
