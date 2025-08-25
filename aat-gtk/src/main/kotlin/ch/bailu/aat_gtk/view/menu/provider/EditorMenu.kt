package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.lib.handler.action.ActionHandler

class EditorMenu(private val edit: EditorSourceInterface): MenuProviderInterface {
    override fun createMenu(): Menu {
        return Menu().apply {
            append(Res.str().edit_save(), "app.editSave")
            append(Res.str().edit_save_copy(), "app.editCopy")
            append(Res.str().edit_save_copy_to(), "app.editCopyTo")
            append(Res.str().edit_inverse(), "app.editInverse")
            appendSubmenu(Res.str().edit_change_type(), Menu().apply {
                append("Track", "app.editTypeTrack")
                append("Route", "app.editTypeRoute")
                append("Way", "app.editTypeWay")

            })
            append(Res.str().edit_simplify(), "app.editSimplify")
            append(Res.str().edit_attach(), "app.editAttach")
            append(Res.str().edit_fix(), "app.editFix")
            append(Res.str().edit_clear(), "app.editClear")
            append(
                Res.str().edit_cut_remaining(),
                "app.editRemaining"
            )
            append(
                Res.str().edit_cut_preceding(),
                "app.editPreceding"
            )
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }

    override fun createActions(app: Application) {
        setAction(app, "editSave") { edit.editor.save()}
        setAction(app, "editCopy", this::saveCopy)
        setAction(app, "editCopyTo", this::saveCopyTo)
        setAction(app, "editInverse")  { edit.editor.inverse() }
        setAction(app, "editTypeTrack")  { edit.editor.setType(GpxType.TRACK) }
        setAction(app, "editTypeRoute") { edit.editor.setType(GpxType.ROUTE) }
        setAction(app, "editTypeWay")  { edit.editor.setType(GpxType.WAY) }
        setAction(app, "editSimplify") { edit.editor.simplify() }
        setAction(app, "editAttach", this::attach)
        setAction(app, "editFix") { edit.editor.fix() }
        setAction(app, "editClear") { edit.editor.clear() }
        setAction(app, "editRemaining") { edit.editor.cutRemaining() }
        setAction(app, "editPreceding") { edit.editor.cutPreceding() }
    }

    private fun saveCopy() {
        // editor.edit.saveTo(file)
    }

    private fun saveCopyTo() {
        // editor.edit.saveTo(destDirectory)
    }

    private fun attach() {
    }

    private fun setAction(app: Application, action: String, onActivate: ()->Unit) {
        ActionHandler.get(app, action).apply {
            disconnectSignals()
            onActivate(onActivate)
        }
    }
}
