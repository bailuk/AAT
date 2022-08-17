package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.solid.SolidDirectorySelectorView
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.foc.FocFile
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class PoiStackView(controller: UiController, app: Application, window: Window) {
    val layout = Box(Orientation.VERTICAL, Layout.margin).apply {
        margin(Layout.margin)
        val buttonBox = Box(Orientation.HORIZONTAL, Layout.margin)

        val sdatabase = SolidPoiDatabase(GtkAppContext.mapDirectory, GtkAppContext)

        buttonBox.append(
            Button.newWithLabelButton(Str(ToDo.translate("Back"))).apply {
                onClicked { controller.back() }
            }
        )
        buttonBox.append(Button.newWithLabelButton(Str(ToDo.translate("Load"))))

        append(buttonBox)
        append(SolidDirectorySelectorView(sdatabase, app, window).layout)
        append(SearchEntry())
        append(PoiList(sdatabase, FocFile("test")).scrolled)
    }
}
