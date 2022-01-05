package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.ui.view.solid.ContextCallback
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Revealer
import ch.bailu.gtk.type.Str

class ContextBar(contextCallback: ContextCallback) {
    val revealer = Revealer()

    init {
        val box = Box(Orientation.HORIZONTAL,3)
        val mapButton = Button.newWithLabelButton(Str("Map"))
        val listButton = Button.newWithLabelButton(Str("List"))
        val detailButton = Button.newWithLabelButton(Str("Detail"))

        mapButton.onClicked { contextCallback.showInMap() }
        detailButton.onClicked { contextCallback.showDetail() }
        listButton.onClicked { contextCallback.showInList() }

        box.append(mapButton)
        box.append(listButton)
        box.append(detailButton)

        revealer.child = box
        revealer.revealChild = GTK.FALSE
    }
}