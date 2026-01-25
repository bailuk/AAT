package ch.bailu.aat.menus

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.Menu
import ch.bailu.aat.R
import ch.bailu.aat.views.preferences.dialog.AbsSelectOverlayDialog
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlay
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayList
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.foc.Foc

class EditorMenu(
    private val appContext: AppContext,
    private val context: Context,
    private val editor: EditorInterface
) : AbsMenu() {

    override fun inflate(menu: Menu) {
        add(menu, R.string.edit_clear) { editor.clear() }
        add(menu, R.string.edit_inverse) { editor.inverse() }
        add(menu, R.string.edit_change_type) { changeType() }
        add(menu, R.string.edit_simplify) { editor.simplify() }
        add(menu, R.string.edit_attach) { attach() }
        add(menu, R.string.edit_fix) { editor.fix() }
        add(menu, R.string.edit_cut_remaining) { editor.cutRemaining() }
        add(menu, R.string.edit_cut_preceding) { editor.cutPreceding() }
        add(menu, ToDo.translate("Undo")) { editor.undo() }
        add(menu, ToDo.translate("Redo")) { editor.redo() }
    }

    override val title: String
        get() = ""

    override fun prepare(menu: Menu) {}


    private fun attach() {
        object : AbsSelectOverlayDialog(appContext, context) {
            override fun onFileSelected(slist: SolidOverlayList<SolidCustomOverlay>, index: Int, file: Foc) {
                appContext.services.insideContext {
                    val handle = appContext.services.getCacheService().getObject(
                        file.path,
                        Obj.Factory()
                    )
                    if (handle is ObjGpx) {
                        if (handle.isReadyAndLoaded()) {
                            editor.attach(handle.getGpxList())
                        }
                    }
                    handle.free()
                    slist.setEnabled(index, false)
                }
            }
        }
    }

    private fun changeType() {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(R.string.edit_change_type)
        dialog.setItems(GpxType.toStrings()) { _: DialogInterface?, i: Int ->
            editor.setType(
                GpxType.fromInteger(i)
            )
        }
        dialog.show()
    }
}
