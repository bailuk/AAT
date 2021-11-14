package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class PreferencesStackView(storage: StorageInterface, window: Window) {
    val layout = Box(Orientation.VERTICAL, 5)
    private val switcher = StackSwitcher()
    private val stack = Stack()

    private val general = GeneralPreferencesView(storage, window)
    private val map = MapPreferencesView(storage, window)

    init {
        val strGeneral = Str(Res.str().p_general())
        val strMap = Str(Res.str().p_map())

        stack.addTitled(general.scrolled, strGeneral, strGeneral)
        stack.addTitled(map.scrolled, strMap, strMap)


        switcher.stack = stack

        layout.borderWidth = 5
        layout.packStart(switcher, GTK.FALSE, GTK.TRUE, 5)
        layout.packStart(stack, GTK.TRUE, GTK.TRUE, 5)

        layout.showAll()
    }
}