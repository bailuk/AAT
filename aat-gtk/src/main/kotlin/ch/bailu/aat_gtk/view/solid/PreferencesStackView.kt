package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.stack.LazyStackView
import ch.bailu.aat_gtk.view.stack.StackViewSelector
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class PreferencesStackView(controller: UiController, storage: StorageInterface, app: Application, window: Window) {

    private val stack = LazyStackView(Stack().apply {
        margin(Layout.margin)
        hexpand = true
        vexpand = true
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

    private val selector = StackViewSelector(stack)

    init {
        val back = Button.newWithLabelButton(Str(ToDo.translate("Back")))
        back.margin(Layout.margin)
        back.onClicked { controller.back() }
        box.append(back)

        box.append(selector.combo)

        layout.append(box)
        layout.append(stack.widget)
        layout.show()
    }

    fun showMap() {
       stack.show(stack.lastIndex())
    }
}
