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
import ch.bailu.aat_lib.service.ServicesInterface

class EditorBarLayer(
    appContext: AppContext,
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
    private val services: ServicesInterface
    private val selector: EditorNodeViewLayer
    private val content: GpxDynLayer

    init {
        services = appContext.services
        content = GpxDynLayer(appContext.storage, mcontext, appContext.services)
        selector = EditorNodeViewLayer(appContext, context, mcontext, edit)

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

    override fun drawInside(p: MapContext) {
        content.drawInside(p)
        if (isBarVisible) {
            selector.drawInside(p)
        }
    }

    override fun drawForeground(mcontext: MapContext) {
        content.drawForeground(mcontext)
        if (isBarVisible) {
            selector.drawForeground(mcontext)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        val editor = edit.editor
        if (v === add) {
            val p = mcontext.mapView.mapViewPosition.center
            editor.add(
                GpxPoint(
                    p,
                    services.elevationService.getElevation(p.latitudeE6, p.longitudeE6).toFloat(),
                    0
                )
            )
        } else if (v === remove) {
            editor.remove()
        } else if (v === up){
            editor.up()
        } else if (v === down){
            editor.down()
        } else if (v === undo){
            editor.undo()
        } else if (v === redo){
            editor.redo()
        } else if (v === menu){
            EditorMenu(v.context, services, editor, edit.file).showAsPopup(v.context, v)
        }
    }

    override fun onShowBar() {
        selector.showAtRight()
    }

    override fun onHideBar() {
        selector.hide()
    }

    override fun onPreferencesChanged(s: StorageInterface, key: String) {
        content.onPreferencesChanged(s, key)
        selector.onPreferencesChanged(s, key)
    }

    override fun onAttached() {}
    override fun onDetached() {}
}
