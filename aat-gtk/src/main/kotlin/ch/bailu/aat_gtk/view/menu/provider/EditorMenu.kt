package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.lib.handler.action.ActionHandler

class EditorMenu(private val editor: EditorInterface, private val app: Application): MenuProvider {
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
        setAction(app, "editSave") { editor.save()}
        setAction(app, "editCopy", this::saveCopy)
        setAction(app, "editCopyTo", this::saveCopyTo)
        setAction(app, "editInverse")  { editor.inverse() }
        setAction(app, "editTypeTrack")  { editor.setType(GpxType.TRACK) }
        setAction(app, "editTypeRoute") { editor.setType(GpxType.ROUTE) }
        setAction(app, "editTypeWay")  { editor.setType(GpxType.WAY) }
        setAction(app, "editSimplify") { editor.simplify() }
        setAction(app, "editAttach", this::attach)
        setAction(app, "editFix") { editor.fix() }
        setAction(app, "editClear") { editor.clear() }
        setAction(app, "editRemaining") { editor.cutRemaining() }
        setAction(app, "editPreceding") { editor.cutPreceding() }
    }


    private fun saveCopy() {
        // editor.saveTo(file)
    }

    private fun saveCopyTo() {
        //editor.saveTo(destDirectory)
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
