package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.ui.view.list.FileList
import ch.bailu.aat_gtk.ui.view.solid.PreferencesStackView
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.gtk.StackTransitionType
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.type.Str

class MainStackView (services: ServicesInterface, storage: StorageInterface, dispatcher: DispatcherInterface, window: Window) {

    val layout = Stack()


    private val preferences = PreferencesStackView(storage, window)
    private val map = MapMainView(services, storage, dispatcher )
    private val fileList = FileList()

    private val strPreferences = Str(Res.str().intro_settings())
    private val strMap = Str(Res.str().p_map())

    init {
        layout.transitionType = StackTransitionType.SLIDE_LEFT
        layout.addTitled(preferences.layout, strPreferences, strPreferences)
        layout.addTitled(map.layout, strMap, strMap)
        layout.addTitled(fileList.vbox, Str("Files"), Str("Files"))
        showMap()
        layout.showAll()
    }

    fun showMap() {
        layout.visibleChild = map.layout
    }

    fun showPreferences() {
        layout.visibleChild = preferences.layout
    }

    fun showFiles() {
        layout.visibleChild = fileList.vbox
    }
}