package ch.bailu.aat.menus

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.Menu
import ch.bailu.aat.R
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog
import ch.bailu.aat.views.preferences.dialog.AbsSelectOverlayDialog
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

class EditorMenu(
    private val appContext: AppContext,
    private val context: Context,
    private val editor: EditorInterface,
    private val file: Foc
) : AbsMenu() {

    override fun inflate(menu: Menu) {
        add(menu, R.string.edit_save,) { editor.save() }
        add(menu, R.string.edit_save_copy) { saveCopy() }
        add(menu, R.string.edit_save_copy_to) { saveCopyTo() }
        add(menu, R.string.edit_inverse) { editor.inverse() }
        add(menu, R.string.edit_change_type) { changeType() }
        add(menu, R.string.edit_simplify) { editor.simplify() }
        add(menu, R.string.edit_attach) { attach() }
        add(menu, R.string.edit_fix) { editor.fix() }
        add(menu, R.string.edit_clear) { editor.clear() }
        add(menu, R.string.edit_cut_remaining) { editor.cutRemaining() }
        add(menu, R.string.edit_cut_preceding) { editor.cutPreceding() }
    }

    override val title: String
        get() = ""

    override fun prepare(menu: Menu) {}
    private fun saveCopy() {
        val sdirectory = SolidDataDirectory(AndroidSolidDataDirectoryDefault(context), FocAndroidFactory(context))
        if (file == AppDirectory.getEditorDraft(sdirectory)) {
            editor.saveTo(
                AppDirectory.getDataDirectory(
                    sdirectory,
                    AppDirectory.DIR_OVERLAY
                )
            )
        } else if (file.hasParent()) editor.saveTo(file.parent())
    }

    private fun saveCopyTo() {
        object : AppSelectDirectoryDialog(appContext, context, file) {
            override fun copyTo(context: Context, srcFile: Foc, destDirectory: Foc) {
                editor.saveTo(destDirectory)
            }
        }
    }

    private fun attach() {
        object : AbsSelectOverlayDialog(context) {
            override fun onFileSelected(slist: SolidCustomOverlayList, index: Int, file: Foc) {
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
