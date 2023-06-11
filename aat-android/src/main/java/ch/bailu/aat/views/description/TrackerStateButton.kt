package ch.bailu.aat.views.description

import android.view.View
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat_lib.description.TrackerStateDescription

class TrackerStateButton(private val scontext: ServiceContext) : ColorNumberView(
    scontext.context, TrackerStateDescription(), AppTheme.bar
), View.OnClickListener {
    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === this) {
            scontext.insideContext { scontext.trackerService.onStartPauseResume() }
        }
    }
}
