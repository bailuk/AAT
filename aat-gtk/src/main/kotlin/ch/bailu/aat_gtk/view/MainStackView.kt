package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.description.CockpitView
import ch.bailu.aat_gtk.view.list.FileList
import ch.bailu.aat_gtk.view.solid.PreferencesStackView
import ch.bailu.aat_lib.description.*
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.gtk.StackTransitionType
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str

class MainStackView (actionHelper: ActionHelper, dispatcher: DispatcherInterface, window: Window) :
    UiController {

    val layout = Stack()


    private val preferences = PreferencesStackView(this, GtkAppContext.storage, window)
    private val map = MapMainView(actionHelper, dispatcher, this)
    private val cockpit = CockpitView()
    private val fileList = FileList(dispatcher)
    private val detail = GpxDetailView(dispatcher, GtkAppContext.storage)

    private val strPreferences = Str(Res.str().intro_settings())
    private val strMap = Str(Res.str().p_map())

    private var lastSelected : Widget = map.overlay

    init {
        layout.transitionType = StackTransitionType.SLIDE_LEFT
        layout.addTitled(preferences.layout, strPreferences, strPreferences)
        layout.addTitled(map.overlay, strMap, strMap)
        layout.addTitled(fileList.vbox, Str("Files"), Str("Files"))
        layout.addTitled(detail.scrolled, Str("Detail"), Str("Detail"))
        layout.addTitled(cockpit.flow, Str("Cockpit"), Str("Cockpit"))

        initCockpit(dispatcher)
        showMap()
    }

    private fun initCockpit(dispatcher: DispatcherInterface) {
        cockpit.add(dispatcher, CurrentSpeedDescription(GtkAppContext.storage), InfoID.LOCATION)
        cockpit.add(dispatcher, AltitudeDescription(GtkAppContext.storage), InfoID.LOCATION)

        cockpit.add(dispatcher, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
        cockpit.add(dispatcher, DistanceDescription(GtkAppContext.storage), InfoID.TRACKER)
        cockpit.add(dispatcher, AverageSpeedDescriptionAP(GtkAppContext.storage), InfoID.TRACKER)
    }

    override fun showCockpit() {
        select(cockpit.flow)
    }

    override fun showMap() {
        select(map.overlay)
    }

    fun showPreferences() {
        select(preferences.layout)
    }

    fun showFiles() {
        select(fileList.vbox)
    }

    override fun showInMap(info: GpxInformation) {
        if (info.boundingBox.hasBounding()) {
            map.map.frameBounding(info.boundingBox)
        }
        showMap()
    }

    override fun showDetail() {
        select(detail.scrolled)
    }

    override fun showInList() {
        showFiles()
    }

    override fun showPreferencesMap() {
        showPreferences()
        preferences.showMap()
    }

    override fun back() {
        layout.visibleChild = lastSelected
    }

    private fun select(widget: Widget) {
        lastSelected = layout.visibleChild
        layout.visibleChild = widget
    }
}
