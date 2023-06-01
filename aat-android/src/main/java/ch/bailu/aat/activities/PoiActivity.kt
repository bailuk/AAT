package ch.bailu.aat.activities

import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.PercentageLayout
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.osm_features.PoiView
import ch.bailu.aat.views.preferences.TitleView
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration
import ch.bailu.aat_lib.search.poi.PoiApi
import ch.bailu.aat_lib.util.fs.AppDirectory
import org.mapsforge.poi.storage.PoiCategory

class PoiActivity : AbsOsmApiActivity() {
    private var multiView: MultiView? = null
    private var poiView: PoiView? = null

    companion object {
        private val KEY = PoiActivity::class.java.simpleName
    }

    public override fun createApiConfiguration(boundingBox: BoundingBoxE6): OsmApiConfiguration {
        return object : PoiApi(appContext, boundingBox) {
            override val selectedCategories: ArrayList<PoiCategory>
                get() {
                    poiView!!.saveSelected(queryFile)
                    return poiView!!.selectedCategories
                }
        }
    }

    override fun createMainContentView(contentView: ContentView): View {
        val linear = LinearLayout(this)
        linear.orientation = LinearLayout.VERTICAL
        linear.addView(TitleView(this, configuration!!.apiName, theme))
        linear.addView(createNodeListView(contentView))
        return linear
    }

    override fun createNodeListView(contentView: ContentView): View {
        return if (AppLayout.isTablet(this)) {
            val mainView = PercentageLayout(this)
            mainView.setOrientation(LinearLayout.HORIZONTAL)
            mainView.add(super.createNodeListView(contentView), 50)
            mainView.add(createPoiListView(), 50)
            mainView
        } else {
            val multiView = MultiView(this, KEY)
            this.multiView = multiView
            multiView.add(super.createNodeListView(contentView))
            multiView.add(createPoiListView())
            contentView.addMvIndicator(multiView)
            multiView
        }
    }

    public override fun addCustomButtons(bar: MainControlBar) {
        if (!AppLayout.isTablet(this)) {
            bar.addMvNext(multiView)
        } else {
            bar.addSpace()
        }
    }

    private fun createPoiListView(): View {
        poiView = PoiView(
            this, appContext,
            configuration!!.baseDirectory.child(AppDirectory.FILE_SELECTION), theme
        )
        theme.background(poiView)
        return poiView!!
    }

    override fun onDestroy() {
        poiView!!.close(serviceContext)
        super.onDestroy()
    }
}
