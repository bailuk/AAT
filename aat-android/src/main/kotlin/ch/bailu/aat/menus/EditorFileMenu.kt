package ch.bailu.aat.menus

import android.content.Context
import android.view.Menu
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog
import ch.bailu.aat_lib.api.ApiController
import ch.bailu.aat_lib.api.brouter.BrouterApi
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

class EditorFileMenu(
    private val appContext: AppContext,
    private val context: Context,
    private val editor: EditorInterface,
    private val file: Foc,
    private val brouterController: ApiController
) : AbsMenu() {

    override val title: String
        get() = ""

    override fun prepare(menu: Menu) {}

    override fun inflate(menu: Menu) {
        add(menu, Res.str().edit_save()) { editor.save() }
        add(menu, Res.str().edit_save_copy()) { saveCopy() }
        add(menu, Res.str().edit_save_copy_to()) { saveCopyTo() }
        add(menu, "\u21d2 ${BrouterApi.NAME}") { brouter() }
    }

    private fun saveCopy() {
        val sdirectory = AndroidSolidDataDirectory(context)
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

    private fun brouter() {
        brouterController.onAction()
    }
}
