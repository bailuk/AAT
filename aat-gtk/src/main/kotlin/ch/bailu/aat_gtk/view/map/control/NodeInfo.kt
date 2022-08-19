package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.lib.extensions.setMarkup
import ch.bailu.aat_lib.html.MarkupBuilderGpx
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class NodeInfo {
    val box = Box(Orientation.VERTICAL,0)

    private val label = Label(Str.NULL)
    private val htmlBuilder = MarkupBuilderGpx(GtkAppContext.storage, PangoMarkupConfig)

    init {
        htmlBuilder.appendHeaderNl("About")
        htmlBuilder.appendBoldNl("name", "hans")
        htmlBuilder.appendNl("place", "lost")

        label.setMarkup(htmlBuilder.toString())
        htmlBuilder.clear()
        box.append(label)

        box.margin(Layout.marginBig)
    }
}
