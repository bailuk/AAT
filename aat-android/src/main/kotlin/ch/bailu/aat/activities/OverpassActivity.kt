package ch.bailu.aat.activities

import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.util.OverpassApi
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.osm.features.OnSelected
import ch.bailu.aat.views.osm.features.OsmFeaturesView
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.lib.filter_list.AbsListItem
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration

class OverpassActivity : AbsOsmApiActivity() {
    private var osmFeatures: OsmFeaturesView? = null
    private var multiView: MultiView? = null

    public override fun createNodeListView(contentView: ContentView): View {
        return if (AppLayout.isTablet(this)) {
            val mainView = PercentageLayout(this)
            mainView.setOrientation(LinearLayout.HORIZONTAL)
            mainView.add(super.createNodeListView(contentView), 50)
            mainView.add(createOsmFeaturesView(), 50)
            mainView
        } else {
            val multiView = MultiView(this, KEY)
            this.multiView = multiView
            multiView.add(super.createNodeListView(contentView))
            multiView.add(createOsmFeaturesView())
            contentView.addMvIndicator(multiView)
            multiView
        }
    }

    private fun createOsmFeaturesView(): View {
        val osmFeatures = OsmFeaturesView(serviceContext)
        osmFeatures.setOnTextSelected { e: AbsListItem, action: OnSelected.Action, variant: String ->
            if (action == OnSelected.Action.Filter) {
                osmFeatures.setFilterText(e.summaryKey)
            } else if (action == OnSelected.Action.Edit) {
                insertLine(variant)
            }
        }
        theme.background(osmFeatures)

        this.osmFeatures = osmFeatures
        return osmFeatures
    }

    override fun onResumeWithService() {
        super.onResumeWithService()
        osmFeatures?.onResume(serviceContext)
    }

    override fun onPause() {
        osmFeatures?.onPause(serviceContext)
        super.onPause()
    }

    public override fun createApiConfiguration(boundingBox: BoundingBoxE6): OsmApiConfiguration {
        return object : OverpassApi(appContext, boundingBox) {
            override val queryString: String
                get() = editorView.toString()
        }
    }

    public override fun addCustomButtons(bar: MainControlBar) {
        val multiView = this.multiView

        if (!AppLayout.isTablet(this) && multiView is View) {
            bar.addMvNext(multiView)
        } else {
            bar.addSpace()
        }
    }

    companion object {
        private val KEY = OverpassActivity::class.java.simpleName
    }
}
