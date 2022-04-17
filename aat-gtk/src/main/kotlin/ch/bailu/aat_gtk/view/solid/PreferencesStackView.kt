package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.util.margin
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class PreferencesStackView(controller: UiController, storage: StorageInterface, app: Application, window: Window) {

    companion object {
        const val MARGIN = 5
    }

    val layout = Box(Orientation.VERTICAL, 0)
    private val box = Box(Orientation.HORIZONTAL, 0)
    private val switcher = StackSwitcher()
    private val stack = Stack()

    private val general = GeneralPreferencesView(storage, app, window)
    private val map = MapPreferencesView(storage, app, window)

    init {
        val strGeneral = Str(Res.str().p_general())
        val strMap = Str(Res.str().p_map())

        val back = Button.newWithLabelButton(Str(ToDo.translate("Back")))
        back.margin(MARGIN)
        back.onClicked { controller.back() }
        box.append(back)

        stack.margin(MARGIN)

        stack.addTitled(general.scrolled, strGeneral, strGeneral)
        stack.addTitled(map.scrolled, strMap, strMap)

        switcher.stack = stack
        switcher.margin(MARGIN)

        layout.append(box)
        layout.append(switcher)
        layout.append(stack)
        stack.hexpand = GTK.TRUE
        stack.vexpand = GTK.TRUE

        layout.show()
    }

    fun showMap() {
        stack.visibleChild = map.scrolled
    }
}
