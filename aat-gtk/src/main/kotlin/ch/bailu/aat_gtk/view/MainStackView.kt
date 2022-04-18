package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.description.CockpitView
import ch.bailu.aat_gtk.view.list.FileList
import ch.bailu.aat_gtk.view.solid.Page
import ch.bailu.aat_gtk.view.solid.PreferencesStackView
import ch.bailu.aat_lib.description.*
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class MainStackView (app: Application, dispatcher: DispatcherInterface, window: Window, private val storage: StorageInterface, private val revealer: ToggleButton) :
    UiController {

    companion object {
        const val KEY = "main-stack"
    }

    val layout = Stack()

    private var revealerRestore = GTK.FALSE

    private val preferences = PreferencesStackView(this, GtkAppContext.storage, app, window)
    private val map = MapMainView(app, dispatcher, this, GtkAppContext, window)
    private val cockpit = CockpitView()
    private val fileList = FileList(app, this, GtkAppContext.storage, GtkAppContext, dispatcher)
    private val detail = GpxDetailView(dispatcher, GtkAppContext.storage)

    private var backTo : Widget = map.overlay
    private val pages = ArrayList<Page>()

    init {
        layout.transitionType = StackTransitionType.SLIDE_LEFT

        pages.add(Page(Str(Res.str().intro_settings()), preferences.layout))
        pages.add(Page(Str(Res.str().p_map()), map.overlay))
        pages.add(Page(Str("List"), fileList.vbox))
        pages.add(Page(Str("Detail"), detail.scrolled))
        pages.add(Page(Str("Cockpit"), cockpit.flow))

        pages.forEach {
            layout.addChild(it.widget)
        }

        initCockpit(dispatcher)

        val index = storage.readInteger(KEY)
        if (index > 0) {
            select(pages[index].widget)
        } else {
            showMap()
        }
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
        revealerRestore = revealer.active
        revealer.active = GTK.FALSE
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
        revealer.active = revealerRestore
        layout.visibleChild = backTo
    }

    override fun showContextBar() {
        revealer.active = GTK.TRUE
    }

    private fun select(widget: Widget) {
        val index = getPageIndex(widget)
        if (index > 0) {
            storage.writeInteger(KEY, index)
            backTo = widget
        }

        layout.visibleChild = widget
    }

    private fun getPageIndex(widget: Widget): Int {
        pages.forEachIndexed { index, page ->
            if (page.widget == widget) {
                return index
            }
        }
        return 0
    }
}
