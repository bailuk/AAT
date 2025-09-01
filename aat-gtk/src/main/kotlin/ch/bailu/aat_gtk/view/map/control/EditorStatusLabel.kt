package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation

class EditorStatusLabel : TargetInterface {
    private val fileNameLabel = Label(InformationUtil.defaultName(InfoID.EDITOR_OVERLAY)).apply {
        addCssClass("status-label")
    }

    val box = Box(Orientation.HORIZONTAL, Layout.MARGIN).apply {
        addCssClass(Strings.CSS_MAP_CONTROL)
        valign = Align.END
        halign = Align.CENTER
        append(fileNameLabel)
    }

    fun hide() {
        box.visible = false
    }

    fun show() {
        box.visible = true
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val name = info.getFile().name

        if (name.isEmpty()) {
            fileNameLabel.setLabel(InformationUtil.defaultName(iid))
        } else {
            fileNameLabel.setLabel(info.getFile().name)
        }
    }
}
