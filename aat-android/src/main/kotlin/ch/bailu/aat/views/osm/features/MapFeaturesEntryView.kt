package ch.bailu.aat.views.osm.features

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat.R
import ch.bailu.aat.menus.MapFeaturesMenu
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListItem
import ch.bailu.aat.util.AppHtml
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.image.SVGAssetView
import ch.bailu.aat_lib.service.icons.IconMapService

class MapFeaturesEntryView(scontext: ServiceContext, private val onSelected: OnSelected, theme: UiTheme) :
    LinearLayout(scontext.getContext()), View.OnClickListener {
    private val text: TextView = TextView(context)
    private val icon: SVGAssetView = SVGAssetView(scontext, R.drawable.open_menu_light)

    private var mapFeaturesListEntry: MapFeaturesListItem? = null

    init {
        if (icon_view_size == 0) {
            icon_view_size = AndroidAppDensity(context).toPixel_i(IconMapService.BIG_ICON_SIZE + 10)
        }

        icon.setOnClickListener(this)
        addView(icon, icon_view_size, icon_view_size)
        addView(text)
        theme.content(text)
    }

    fun set(listEntry: MapFeaturesListItem) {
        mapFeaturesListEntry = listEntry

        icon.setImageObject(listEntry.osmKey, listEntry.osmValue)
        text.text = AppHtml.fromHtml(listEntry.html)
    }

    override fun onClick(view: View) {
        val listEntry = this.mapFeaturesListEntry

        if (view === icon && listEntry is MapFeaturesListItem) {
            MapFeaturesMenu(listEntry, onSelected).showAsPopup(context, view)
        }
    }

    companion object {
        private var icon_view_size = 0
    }
}
