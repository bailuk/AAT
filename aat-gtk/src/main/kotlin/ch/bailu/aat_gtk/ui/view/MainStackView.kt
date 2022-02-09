package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.ui.view.description.CockpitView
import ch.bailu.aat_gtk.ui.view.list.FileList
import ch.bailu.aat_gtk.ui.view.solid.ContextCallback
import ch.bailu.aat_gtk.ui.view.solid.PreferencesStackView
import ch.bailu.aat_lib.description.*
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.gtk.StackTransitionType
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str

class MainStackView (actionHelper: ActionHelper, dispatcher: DispatcherInterface, window: Window) : ContextCallback {

    val layout = Stack()

    private val preferences = PreferencesStackView(GtkAppContext.storage, window)
    private val map = MapMainView(actionHelper, dispatcher)
    private val cockpit = CockpitView()
    private val fileList = FileList(dispatcher)
    private val detail = GpxDetailView(dispatcher, GtkAppContext.storage)

    private val strPreferences = Str(Res.str().intro_settings())
    private val strMap = Str(Res.str().p_map())

    init {
        layout.transitionType = StackTransitionType.SLIDE_LEFT
        layout.addTitled(preferences.layout, strPreferences, strPreferences)
        layout.addTitled(map.overlay, strMap, strMap)
        layout.addTitled(fileList.vbox, Str("Files"), Str("Files"))
        layout.addTitled(detail.scrolled, Str("Detail"), Str("Detail"))
        layout.addTitled(cockpit.layout, Str("Cockpit"), Str("Cockpit"))

        initCockpit(dispatcher)
        showMap()
        layout.show()
    }

    private fun initCockpit(dispatcher: DispatcherInterface) {
        cockpit.add(dispatcher, CurrentSpeedDescription(GtkAppContext.storage), InfoID.LOCATION)
        cockpit.add(dispatcher, AltitudeDescription(GtkAppContext.storage), InfoID.LOCATION)

        cockpit.add(dispatcher, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
        cockpit.add(dispatcher, DistanceDescription(GtkAppContext.storage), InfoID.TRACKER)
        cockpit.add(dispatcher, AverageSpeedDescriptionAP(GtkAppContext.storage), InfoID.TRACKER)
    }

    fun showCockpit() {
        layout.visibleChild = cockpit.layout
        cockpit.layout()
    }

    fun showMap() {
        layout.visibleChild = map.overlay
    }

    fun showPreferences() {
        layout.visibleChild = preferences.layout
    }

    fun showFiles() {
        layout.visibleChild = fileList.vbox
    }

    override fun showInMap(info: GpxInformation) {
        map.map.frameBounding(info.boundingBox)
        showMap()
    }

    override fun showDetail() {
        layout.visibleChild = detail.scrolled
    }

    override fun showInList() {
        showFiles()
    }
}
