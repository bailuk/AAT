package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.resources.Res

class EditorMenu(private val editor: EditorSourceInterface): MenuProvider {
    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .label(Res.str().edit_save()) {editor.editor.save()}
            .label(Res.str().edit_save_copy(), this::saveCopy)
            .label(Res.str().edit_save_copy_to(), this::saveCopyTo)
            .label(Res.str().edit_inverse()) { editor.editor.inverse() }
            .submenu(Res.str().edit_change_type(), MenuModelBuilder()
                .label("Track") { editor.editor.setType(GpxType.TRACK) }
                .label("Route") { editor.editor.setType(GpxType.ROUTE) }
                .label("Way") { editor.editor.setType(GpxType.WAY) }
            )
            .label(Res.str().edit_simplify()) { editor.editor.simplify() }
            .label(Res.str().edit_attach(), this::attach)
            .label(Res.str().edit_fix()) { editor.editor.fix() }
            .label(Res.str().edit_clear()) { editor.editor.clear() }
            .label(Res.str().edit_cut_remaining()) {editor.editor.cutRemaining() }
            .label(Res.str().edit_cut_preceding()) {editor.editor.cutPreceding() }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }


    private fun saveCopy() {
        // editor.saveTo(file)
    }

    private fun saveCopyTo() {
        //editor.saveTo(destDirectory)
    }


    private fun attach() {
    }

}