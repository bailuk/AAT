package ch.bailu.aat.views.tileremover

import android.app.Activity
import android.widget.RadioGroup
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.SolidTrimIndex
import ch.bailu.aat.services.tileremover.SourceSummaries
import ch.bailu.aat.util.ui.theme.UiTheme

class TileSummariesView(activity: Activity, theme: UiTheme) : RadioGroup(activity) {
    private val views = ArrayList<TileSummaryView>(SourceSummaries.SUMMARY_SIZE)
    private val builder = StringBuilder(100)
    private val theme: UiTheme

    init {
        orientation = VERTICAL
        this.theme = theme
    }

    fun updateInfo(summaries: SourceSummaries) {
        if (summaries.size() != views.size) {
            val selected = SolidTrimIndex(Storage(context)).value

            // remove views
            for (i in views.size - 1 downTo summaries.size()) {
                views[i].destroy()
                views.removeAt(i)
            }

            // addLayer views
            for (i in views.size until summaries.size()) {
                views.add(TileSummaryView(this, i, theme))
                views[i].select(selected)
            }

            // update title
            var i = 0
            while (i < summaries.size() && i < views.size) {
                views[i].setTitle(summaries[i].name)
                i++
            }
            if (selected >= summaries.size()) {
                views[0].select()
                SolidTrimIndex(Storage(context)).value = 0
            }
        }
        if (summaries.size() == views.size) {
            // update text
            for (i in views.indices) {
                views[i].displaySummaryReport(builder, summaries[i])
            }
        }
    }
}
