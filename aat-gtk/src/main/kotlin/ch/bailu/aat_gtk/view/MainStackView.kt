package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.app.TimeStation
import ch.bailu.aat_gtk.view.description.CockpitView
import ch.bailu.aat_gtk.view.list.FileList
import ch.bailu.aat_gtk.view.share.LazyStackView
import ch.bailu.aat_gtk.view.solid.PreferencesStackView
import ch.bailu.aat_lib.description.*
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*

class MainStackView(
    app: Application,
    dispatcher: DispatcherInterface,
    window: Window,
    storage: StorageInterface,
    private val revealer: ToggleButton
) :
    UiController {

    companion object {
        const val KEY = "main-stack"
        const val INDEX_MAP = 0
        const val INDEX_FILES = 1
        const val INDEX_DETAIL = 2
        const val INDEX_COCKPIT = 3


    }

    private val stack = LazyStackView(Stack().apply {
        transitionType = StackTransitionType.SLIDE_LEFT
    }, KEY, storage)

    val widget
        get() = stack.widget

    private var revealerRestore = GTK.FALSE


    private val map = stack.add("Map") {
        MapMainView(app, dispatcher, this, GtkAppContext, window).overlay
    }

    private val files = stack.add("Files") {
        FileList(app, this, GtkAppContext.storage, GtkAppContext, dispatcher).vbox
    }

    private val detail = stack.add("Detail") {
        GpxDetailView(dispatcher, GtkAppContext.storage).scrolled
    }

    private val cockpit = stack.add("Cockpit") {
        CockpitView().apply {
            add(dispatcher, CurrentSpeedDescription(GtkAppContext.storage), InfoID.LOCATION)
            add(dispatcher, AltitudeDescription(GtkAppContext.storage), InfoID.LOCATION)

            add(dispatcher, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
            add(dispatcher, DistanceDescription(GtkAppContext.storage), InfoID.TRACKER)
            add(dispatcher, AverageSpeedDescriptionAP(GtkAppContext.storage), InfoID.TRACKER)
        }.flow
    }

    private val preferences = stack.add("Preferences") {
        PreferencesStackView(this, GtkAppContext.storage, app, window).layout
    }

    private var backTo = INDEX_MAP

    init {
        TimeStation.log("init -> stack.restore()")
        stack.restore()
        TimeStation.log("init <- stack.restore()")
    }


    override fun showCockpit() {
        stack.show(INDEX_COCKPIT)
        backTo = INDEX_COCKPIT
    }

    override fun showMap() {
        stack.show(INDEX_MAP)
        backTo = INDEX_MAP
    }

    fun showPreferences() {
        revealerRestore = revealer.active
        revealer.active = GTK.FALSE
        preferences.show()
    }

    fun showFiles() {
        stack.show(INDEX_FILES)
        backTo = INDEX_FILES
    }

    override fun showInMap(info: GpxInformation) {
        if (info.boundingBox.hasBounding()) {

            //map.map.frameBounding(info.boundingBox)
        }
        showMap()
    }

    override fun showDetail() {
        stack.show(INDEX_DETAIL)
        backTo = INDEX_DETAIL
    }

    override fun showInList() {
        showFiles()
    }

    override fun showPreferencesMap() {
        showPreferences()
        //preferences.showMap()
    }

    override fun back() {
        revealer.active = revealerRestore
        stack.show(backTo)
    }

    override fun showContextBar() {
        revealer.active = GTK.TRUE
    }
}
