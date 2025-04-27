package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class NavigationViewBar(controller: UiControllerInterface) {
    val leftButton = Button().apply {
        addCssClass(Strings.mapControl)
        iconName = Icons.goPreviousSymbolic
        onClicked {
            controller.hideMap()
        }
    }

    val rightButton = Button().apply {
        addCssClass(Strings.mapControl)
        iconName = Icons.goNextSymbolic
        onClicked {
            controller.showPoi()
        }
    }
    val layout = Box(Orientation.HORIZONTAL, 0).apply {
        addCssClass(Strings.linked)
        append(leftButton)
        append(rightButton)

        halign = Align.END
        valign = Align.START
        margin(Layout.margin)
    }
}
