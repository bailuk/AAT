package ch.bailu.aat.map.layer

import android.content.Context
import android.view.View
import ch.bailu.aat.R
import ch.bailu.aat.menus.EditorFileMenu
import ch.bailu.aat.menus.EditorMenu
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.api.brouter.BrouterController
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class EditorBarLayer(
    private val appContext: AppContext,
    context: Context,
    private val mcontext: MapContext,
    dispatcher: DispatcherInterface,
    iid: Int,
    private val edit: EditorSourceInterface
) : ControlBarLayer(
    mcontext, ControlBar(context, getOrientation(Position.LEFT), AppTheme.bar), Position.LEFT
) {
    private val menu: View = bar.addImageButton(R.drawable.open_menu)
    private val add: View = bar.addImageButton(R.drawable.list_add)
    private val remove: View = bar.addImageButton(R.drawable.list_remove)
    private val up: View = bar.addImageButton(R.drawable.go_up)
    private val down: View = bar.addImageButton(R.drawable.go_down)
    private val selector = EditorNodeViewLayer(appContext, context, mcontext, edit)
    private val file: View = bar.addImageButton(R.drawable.edit_select_all_inverse)
    private val brouterController = BrouterController(appContext, edit)

    init {
        ToolTip.set(add, Res.str().tt_edit_add())
        ToolTip.set(remove, Res.str().tt_edit_remove())
        ToolTip.set(up, Res.str().tt_edit_up())
        ToolTip.set(down, Res.str().tt_edit_down())
        dispatcher.addTarget(selector, iid)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        selector.onLayout(changed, l, t, r, b)
    }

    override fun drawInside(mcontext: MapContext) {
        if (isBarVisible) {
            selector.drawInside(mcontext)
        }
    }

    override fun drawForeground(mcontext: MapContext) {
        if (isBarVisible) {
            selector.drawForeground(mcontext)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        val editor = edit.editor

        when(v) {
            add -> {
                val pos = mcontext.getMapView().getMapViewPosition().center
                val ele = appContext.services.getElevationService().getElevation(pos.latitudeE6, pos.longitudeE6).toFloat()
                editor.add(GpxPoint(pos, ele,0))
            }
            remove -> editor.remove()
            up -> editor.up()
            down -> editor.down()
            menu -> EditorMenu(appContext, v.context, editor).showAsPopup(v.context, v)
            file -> EditorFileMenu(appContext, v.context, editor, edit.file, brouterController).showAsPopup(v.context, v)
        }
    }

    override fun onShowBar() {
        selector.showAtRight()
    }

    override fun onHideBar() {
        selector.hide()
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        selector.onPreferencesChanged(storage, key)
    }

    override fun onAttached() {}
    override fun onDetached() {}
}
