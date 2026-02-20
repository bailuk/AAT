package ch.bailu.aat.activities

import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.api.OsmApiController
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.osm.poi.PoiView
import ch.bailu.aat.views.preferences.TitleView
import ch.bailu.aat_lib.api.ApiConfiguration
import ch.bailu.aat_lib.api.poi.PoiApi
import ch.bailu.aat_lib.util.fs.AppDirectory
import org.mapsforge.poi.storage.PoiCategory

class PoiActivity : AbsOsmApiActivity() {
    private var multiView: MultiView? = null
    private var poiView: PoiView? = null

    companion object {
        private val KEY = PoiActivity::class.java.simpleName
    }

    public override fun createApiConfiguration(): ApiConfiguration {
        return object : PoiApi(appContext) {
            override val selectedCategories: ArrayList<PoiCategory>
                get() {
                    poiView?.saveSelected(queryFile)
                    return poiView!!.selectedCategories
                }
        }
    }

    override fun createMainContentView(contentView: ContentView): View {
        val linear = LinearLayout(this)
        linear.orientation = LinearLayout.VERTICAL
        val title = controller?.api?.apiName ?: ""
        linear.addView(TitleView(this, title, theme))
        linear.addView(createNodeListView(contentView))
        return linear
    }

    override fun createNodeListView(contentView: ContentView): View {
        return if (AppLayout.isTablet(this)) {
            val mainView = PercentageLayout(this)
            mainView.setOrientation(LinearLayout.HORIZONTAL)
            mainView.add(super.createNodeListView(contentView), 50)
            controller?.apply {
                mainView.add(createPoiListView(this), 50)
            }
            mainView
        } else {
            val multiView = MultiView(this, KEY)
            this.multiView = multiView
            multiView.add(super.createNodeListView(contentView))
            controller?.apply {
                multiView.add(createPoiListView(this))
            }
            contentView.addMvIndicator(multiView)
            multiView
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

    private fun createPoiListView(controller: OsmApiController): View {
        val poiView = PoiView(this, appContext, controller.api.baseDirectory.child(AppDirectory.FILE_SELECTION), theme)
        this.poiView = poiView
        theme.background(poiView)
        return poiView
    }

    override fun onDestroy() {
        poiView?.close(serviceContext)
        super.onDestroy()
    }
}
