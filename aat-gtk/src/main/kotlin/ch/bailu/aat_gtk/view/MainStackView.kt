package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.dispatcher.LogOnContentUpdate
import ch.bailu.aat_gtk.dispatcher.SelectedSource
import ch.bailu.aat_gtk.view.search.PoiStackView
import ch.bailu.aat_gtk.view.solid.PreferencesStackView
import ch.bailu.aat_gtk.view.stack.LazyStackView
import ch.bailu.aat_gtk.view.toplevel.CockpitView
import ch.bailu.aat_gtk.view.toplevel.DetailView
import ch.bailu.aat_gtk.view.toplevel.MapMainView
import ch.bailu.aat_gtk.view.toplevel.list.FileList
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.CustomFileSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.gtk.StackTransitionType
import ch.bailu.gtk.gtk.ToggleButton
import ch.bailu.gtk.gtk.Window
import org.mapsforge.core.model.BoundingBox

class MainStackView (
    app: Application,
    dispatcher: DispatcherInterface,
    selectedSource: SelectedSource,
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

    private val customFileSource = CustomFileSource(GtkAppContext)

    private var preferencesStackView: PreferencesStackView?  = null
    private var mapView: MapMainView? = null

    private val stack = LazyStackView(Stack().apply {
        transitionType = StackTransitionType.SLIDE_LEFT
    }, KEY, storage)

    val widget
        get() = stack.widget

    private var revealerRestore = false


    private val preferences: LazyStackView.LazyPage
    private val poi: LazyStackView.LazyPage

    private var backTo = INDEX_MAP

    init {
        LogOnContentUpdate(dispatcher)

        dispatcher.addSource(customFileSource)

        stack.add("Map") {
            val mapView = MapMainView(app, dispatcher, this, GtkAppContext, window)
            this.mapView = mapView
            mapView.onAttached()
            dispatcher.requestUpdate()
            mapView.overlay
        }

        stack.add("Files") {
            FileList(app, GtkAppContext.storage, GtkAppContext, this).vbox
        }

        stack.add("Detail") {
            val result = DetailView(Dispatcher().apply { addSource(selectedSource); onResume() }, GtkAppContext.storage).scrolled
            dispatcher.requestUpdate()
            result
        }

        stack.add("Cockpit") {
            val result = CockpitView().apply {
                addDefaults((dispatcher))
            }.scrolledWindow
            dispatcher.requestUpdate()
            result
        }

        preferences = stack.add("Preferences") {
            val stackView = PreferencesStackView(this, GtkAppContext.storage, app, window)
            preferencesStackView = stackView
            stackView.layout
        }

        poi = stack.add("POI") {
            val stackView = PoiStackView(this, app, window)
            stackView.layout
        }

        stack.restore()
    }


    override fun showCockpit() {
        stack.show(INDEX_COCKPIT)
        backTo = INDEX_COCKPIT
    }

    override fun showMap() {
        stack.show(INDEX_MAP)
        backTo = INDEX_MAP
    }

    override fun showPreferences() {
        revealerRestore = revealer.active
        revealer.active = false
        preferences.show()
    }

    override fun showPoi() {
        revealerRestore = revealer.active
        revealer.active = false
        poi.show()
    }

    override fun showFiles() {
        stack.show(INDEX_FILES)
        backTo = INDEX_FILES
    }

    override fun frameInMap(info: GpxInformation) {
        if (info.boundingBox.hasBounding()) {
            showMap()
            mapView?.map?.frameBounding(info.boundingBox)
        }
    }

    override fun centerInMap(info: GpxInformation) {
        TODO("Not yet implemented")
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
        preferencesStackView?.showMap()
    }

    override fun back() {
        revealer.active = revealerRestore
        stack.show(backTo)
    }

    override fun showContextBar() {
        revealer.active = true
    }

    override fun getMapBounding(): BoundingBoxE6 {
        val bounding = mapView?.map?.boundingBox
        if (bounding is BoundingBox) {
            return BoundingBoxE6(bounding)
        }
        return BoundingBoxE6.NULL_BOX
    }

    override fun load(info: GpxInformation) {
        customFileSource.setFileID(info.file.toString())
        showContextBar()
    }

    fun onDestroy() {
        // IMPORTANT this saves map position
        mapView?.onDetached()
    }
}
