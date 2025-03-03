package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class LeafletBar(controller: UiControllerInterface) {
    val layout = Box(Orientation.HORIZONTAL, 0).apply {
        addCssClass(Strings.linked)
        append(Button().apply {
            addCssClass(Strings.mapControl)
            iconName = Icons.goPreviousSymbolic
            onClicked {
                controller.hideMap()
            }
        })
        append(Button().apply {
            addCssClass(Strings.mapControl)
            iconName = Icons.goNextSymbolic
            onClicked {
                controller.showPoi()
            }
        })
    }
}
