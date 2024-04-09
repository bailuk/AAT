package ch.bailu.aat.map.layer

import android.content.Context
import android.view.View
import ch.bailu.aat.R
import ch.bailu.aat.menus.EditorMenu
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
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
    private val undo: View = bar.addImageButton(R.drawable.edit_redo)
    private val redo: View = bar.addImageButton(R.drawable.edit_redo)
    private val content: GpxDynLayer = GpxDynLayer(appContext.storage, mcontext, appContext.services)
    private val selector = EditorNodeViewLayer(appContext, context, mcontext, edit)

    init {
        ToolTip.set(add, Res.str().tt_edit_add())
        ToolTip.set(remove, Res.str().tt_edit_remove())
        ToolTip.set(up, Res.str().tt_edit_up())
        ToolTip.set(down, Res.str().tt_edit_down())
        ToolTip.set(redo, Res.str().tt_edit_redo())
        ToolTip.set(undo, Res.str().tt_edit_undo())
        dispatcher.addTarget(selector, iid)
        dispatcher.addTarget(content, iid)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        selector.onLayout(changed, l, t, r, b)
    }

    override fun drawInside(mcontext: MapContext) {
        content.drawInside(mcontext)
        if (isBarVisible) {
            selector.drawInside(mcontext)
        }
    }

    override fun drawForeground(mcontext: MapContext) {
        content.drawForeground(mcontext)
        if (isBarVisible) {
            selector.drawForeground(mcontext)
        }
    }

    override fun onClick(view: View) {
        super.onClick(view)
        val editor = edit.editor

        when(view) {
            add -> {
                val pos = mcontext.getMapView().getMapViewPosition().center
                val ele = appContext.services.elevationService.getElevation(pos.latitudeE6, pos.longitudeE6).toFloat()
                editor.add(GpxPoint(pos, ele,0))
            }
            remove -> editor.remove()
            up -> editor.up()
            down -> editor.down()
            undo -> editor.undo()
            redo -> editor.redo()
            menu -> EditorMenu(appContext, view.context, editor, edit.file).showAsPopup(view.context, view)
        }
    }

    override fun onShowBar() {
        selector.showAtRight()
    }

    override fun onHideBar() {
        selector.hide()
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        content.onPreferencesChanged(storage, key)
        selector.onPreferencesChanged(storage, key)
    }

    override fun onAttached() {}
    override fun onDetached() {}
}
