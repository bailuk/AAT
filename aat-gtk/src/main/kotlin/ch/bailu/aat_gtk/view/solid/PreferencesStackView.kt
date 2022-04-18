package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.UiController
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
        const val KEY = "preferences-stack"
    }

    val layout = Box(Orientation.VERTICAL, 0)
    private val box = Box(Orientation.HORIZONTAL, 0)
    private val stack = Stack()

    private val combo = ComboBoxText()
    private val pages = ArrayList<Page>()


    init {
        val back = Button.newWithLabelButton(Str(ToDo.translate("Back")))
        back.margin(MARGIN)
        back.onClicked { controller.back() }
        box.append(back)

        stack.margin(MARGIN)

        addPages(app, window, storage)

        combo.onChanged {
            stack.visibleChild = pages[combo.active].widget
            storage.writeInteger(KEY, combo.active)
        }

        combo.margin(MARGIN)
        box.append(combo)

        layout.append(box)
        layout.append(stack)
        stack.hexpand = GTK.TRUE
        stack.vexpand = GTK.TRUE
        stack.transitionType = StackTransitionType.CROSSFADE

        loadPage(storage)
        layout.show()
    }

    fun showMap() {
        combo.active = pages.lastIndex
    }

    private fun addPages(app: Application, window: Window, storage: StorageInterface) {
        pages.add(Page(Str(Res.str().p_general()), GeneralPreferencesView(storage, app, window).scrolled))
        for (i in 0 until SolidPresetCount.DEFAULT) {
            val activity = ActivityPreferencesView(storage, i)
            pages.add(Page(Str(activity.title), activity.scrolled))
        }
        pages.add(Page(Str(Res.str().p_map()), MapPreferencesView(storage, app, window).scrolled))

        pages.forEach {
            combo.insertText(-1, it.title)
            stack.addChild(it.widget)
        }
    }

    private fun loadPage(storage: StorageInterface) {
        val index = storage.readInteger(KEY)
        stack.visibleChild = pages[index].widget
        combo.active = index
    }
}

data class Page(val title: Str, val widget: Widget)
