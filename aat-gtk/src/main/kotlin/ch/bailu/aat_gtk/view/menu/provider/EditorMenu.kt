package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.lib.handler.action.ActionHandler

class EditorMenu(private val edit: EditorSourceInterface): MenuProviderInterface {
    override fun createMenu(): Menu {
        return Menu().apply {
            append(Res.str().edit_clear(), "app.editClear")
            append(Res.str().edit_inverse(), "app.editInverse")
            appendSubmenu(Res.str().edit_change_type(), Menu().apply {
                GpxType.toStrings().forEach { string ->
                    append(string, "app.editType${string}")
                }
            })
            append(Res.str().edit_simplify(), "app.editSimplify")
            append(Res.str().edit_attach(), "app.editAttach")
            append(Res.str().edit_fix(), "app.editFix")
            append(
                Res.str().edit_cut_remaining(),
                "app.editRemaining"
            )
            append(
                Res.str().edit_cut_preceding(),
                "app.editPreceding"
            )
            append(ToDo.translate("Redo"), "app.editRedo")
            append(ToDo.translate("Undo"), "app.editUndo")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }

    override fun createActions(app: Application) {
        setAction(app, "editClear") { edit.editor.clear() }
        setAction(app, "editInverse")  { edit.editor.inverse() }
        GpxType.toStrings().forEachIndexed { index, string ->
            setAction(app, "editType${string}")  { edit.editor.setType(GpxType.fromInteger(index)) }
        }
        setAction(app, "editSimplify") { edit.editor.simplify() }
        setAction(app, "editAttach", this::attach)
        setAction(app, "editFix") { edit.editor.fix() }
        setAction(app, "editRemaining") { edit.editor.cutRemaining() }
        setAction(app, "editPreceding") { edit.editor.cutPreceding() }
        setAction(app, "editUndo") { edit.editor.undo() }
        setAction(app, "editRedo") { edit.editor.redo() }
    }

    override fun updateActionValues(app: Application) {}

    private fun attach() {
    }

    private fun setAction(app: Application, action: String, onActivate: ()->Unit) {
        ActionHandler.get(app, action).apply {
            disconnectSignals()
            onActivate(onActivate)
        }
    }
}
