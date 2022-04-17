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
    private val stack = Stack()

    private val general = GeneralPreferencesView(storage, app, window)
    private val activity = ActivityPreferencesView(storage, 0)
    private val map = MapPreferencesView(storage, app, window)

    private val switcher = Box(Orientation.HORIZONTAL, 0)

    init {
        val strGeneral = Str(Res.str().p_general())
        val strMap = Str(Res.str().p_map())

        val back = Button.newWithLabelButton(Str(ToDo.translate("Back")))
        back.margin(MARGIN)
        back.onClicked { controller.back() }
        box.append(back)

        stack.margin(MARGIN)

        stack.addChild(general.scrolled)
        stack.addChild(activity.scrolled)
        stack.addChild(map.scrolled)

        switcher.append(Button.newWithLabelButton(strGeneral).apply {
            onClicked {
                stack.visibleChild = general.scrolled
            }
        })

        switcher.append(Button.newWithLabelButton(Str(activity.title)).apply {
            onClicked {
                stack.visibleChild = activity.scrolled
            }
        })

        switcher.append(Button.newWithLabelButton(strMap).apply {
            onClicked {
                stack.visibleChild = map.scrolled
            }
        })

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
