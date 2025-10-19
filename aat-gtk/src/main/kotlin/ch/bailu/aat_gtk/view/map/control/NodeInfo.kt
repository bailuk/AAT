package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.html.MarkupBuilderGpx
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.ScrolledWindow
import ch.bailu.gtk.type.Str

class NodeInfo {
    private val markupBuilder = MarkupBuilderGpx(GtkAppContext.storage, PangoMarkupConfig)

    private val label = Label(Str.NULL).apply {
        margin(Layout.MARGIN)
        this.selectable = true
    }

    private val scrolled = ScrolledWindow().apply {
        child = label
        setSizeRequest(Layout.WINDOW_WIDTH-Layout.BAR_SIZE*2,Layout.WINDOW_HEIGHT/5)
    }

    val box = Box(Orientation.VERTICAL,0).apply {
        addCssClass(Strings.CSS_MAP_CONTROL)
        append(scrolled)
        valign = Align.START
        halign = Align.CENTER
        visible = false
    }

    fun showCenter() {
        box.halign = Align.CENTER
        box.visible = true
    }

    fun hide() {
        box.visible = false
    }

    fun displayNode(info: GpxInformation, node: GpxPointNode, index: Int) {
        markupBuilder.appendInfo(info, index)
        markupBuilder.appendNode(node, info)
        markupBuilder.appendAttributes(node.getAttributes())
        label.setMarkup(markupBuilder.toString())
        markupBuilder.clear()
    }
}
