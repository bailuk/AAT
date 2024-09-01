package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class EditorStatusLabel : TargetInterface {
    private val defaultNameLabel = Label(Str.NULL).apply {
        addCssClass("status-label-bold")
    }
    private val fileNameLabel = Label(Str.NULL).apply {
        addCssClass("status-label")
    }
    private val statusLabel = Label(Str.NULL).apply {
        addCssClass("status-label")
    }

    val box = Box(Orientation.HORIZONTAL, Layout.margin).apply {
        addCssClass(Strings.mapControl)
        margin(Layout.margin)
        valign = Align.END
        halign = Align.CENTER
        append(defaultNameLabel)
        append(fileNameLabel)
        append(statusLabel)
    }

    fun hide() {
        box.visible = false
    }

    fun show() {
        box.visible = true
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        defaultNameLabel.setLabel(InformationUtil.defaultName(iid))
        fileNameLabel.setLabel(info.getFile().name)
    }
}
