package ch.bailu.aat.views.preferences

import android.app.Activity
import android.view.View
import android.view.View.OnClickListener
import ch.bailu.aat.util.AppPermission
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.resources.ToDo

class RequestPermissionsView (private val activityContext: Activity,
                              theme: UiTheme
) :
    LabelTextView(activityContext, ToDo.translate("Request permissions"), theme),
    OnClickListener {
    init {
        setOnClickListener(this)
        theme.button(this)
    }

    override fun onClick(view: View) {
        AppPermission.requestFromUser(activityContext)
    }
}
