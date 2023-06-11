package ch.bailu.aat.map.layer.control

import android.content.Context
import android.view.View
import ch.bailu.aat.R
import ch.bailu.aat.menus.LocationMenu
import ch.bailu.aat.menus.MapMenu
import ch.bailu.aat.menus.MapQueryMenu
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.util.ui.ToolTip
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidLegend
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.resources.Res

class InformationBarLayer(
    appContext: AppContext,
    private val context: Context,
    private val mcontext: MapContext,
    dispatcher: DispatcherInterface
) : ControlBarLayer(mcontext, ControlBar(context, getOrientation(Position.RIGHT), AppTheme.bar), Position.RIGHT) {

    private val map = bar.addImageButton(R.drawable.open_menu)
    private val search = bar.addImageButton(R.drawable.edit_find)
    private val location = bar.addImageButton(R.drawable.find_location)
    private val selector = NodeViewLayer(appContext, context, mcontext)

    init {
        val storage: StorageInterface = appContext.storage
        val sgrid = SolidMapGrid(storage, mcontext.solidKey)
        val slegend = SolidLegend(storage, mcontext.solidKey)
        val grid = bar.addSolidIndexButton(sgrid)
        val legend = bar.addSolidIndexButton(slegend)

        ToolTip.set(grid, Res.str().tt_info_grid())
        ToolTip.set(legend, Res.str().tt_info_legend())
        ToolTip.set(location, Res.str().tt_info_location())

        dispatcher.addTarget(selector, InfoID.ALL)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v === map) {
            MapMenu(context, mcontext).showAsPopup(v.getContext(), v)
        } else if (v === search) {
            MapQueryMenu(context, mcontext).showAsPopup(v.getContext(), v)
        } else if (v === location) {
            LocationMenu(context, mcontext.mapView).showAsPopup(v.getContext(), location)
        }
    }

    override fun onShowBar() {
        selector.showAtLeft()
    }

    override fun onHideBar() {
        selector.hide()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        selector.onLayout(changed, l, t, r, b)
    }

    override fun drawForeground(mcontext: MapContext) {
        if (isBarVisible) selector.drawForeground(mcontext)
    }

    override fun drawInside(mcontext: MapContext) {
        if (isBarVisible) selector.drawInside(mcontext)
    }

    override fun onAttached() {}
    override fun onDetached() {}
    override fun onPreferencesChanged(s: StorageInterface, key: String) {
        selector.onPreferencesChanged(s, key)
    }
}
