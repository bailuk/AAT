package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.share.LazyStackView
import ch.bailu.aat_gtk.view.util.margin
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class PreferencesStackView(controller: UiController, storage: StorageInterface, app: Application, window: Window) {

    companion object {
        const val MARGIN = 5
    }

    private val stack = LazyStackView(Stack().apply {
        margin(MARGIN)
        hexpand = GTK.TRUE
        vexpand = GTK.TRUE
        transitionType = StackTransitionType.CROSSFADE

    },"preferences-stack", storage).apply {
        add(Res.str().p_general()) {
            GeneralPreferencesView(storage, app, window).scrolled
        }

        for (i in 0 until SolidPresetCount.DEFAULT) {
            val activity = ActivityPreferencesView(storage, i)
            add(activity.title) {
                activity.scrolled
            }
        }
        add(Res.str().p_map()) {
            MapPreferencesView(storage, app, window).scrolled
        }
    }

    val layout = Box(Orientation.VERTICAL, 0)
    private val box = Box(Orientation.HORIZONTAL, 0)

    private val combo = stack.createCombo()

    init {
        val back = Button.newWithLabelButton(Str(ToDo.translate("Back")))
        back.margin(MARGIN)
        back.onClicked { controller.back() }
        box.append(back)

        combo.onChanged {
            stack.show(combo.active)
        }

        combo.margin(MARGIN)
        combo.active = stack.restore()

        box.append(combo)

        layout.append(box)
        layout.append(stack.widget)
        layout.show()
    }

    fun showMap() {
       stack.show(stack.lastIndex())
    }
}
