package ch.bailu.aat_gtk.view.toplevel.list

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.provider.SolidOverlaySelectorMenu
import ch.bailu.aat_gtk.view.solid.SolidDirectoryQueryComboView
import ch.bailu.aat_gtk.view.toplevel.list.menu.FileContextMenu
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.lib.bridge.ListIndex

class FileList(
    application: Application,
    storage: StorageInterface,
    focFactory: FocFactory,
    uiController: UiController
) {


    private val descriptions = arrayOf(
        DateDescription(),
        DistanceDescription(GtkAppContext.storage),
        AverageSpeedDescription(GtkAppContext.storage),
        TimeDescription())


    val vbox = Box(Orientation.VERTICAL, 12)
    private val listIndex = ListIndex()

    private val iteratorSimple = IteratorSimple(GtkAppContext)
    private val items = HashMap<ListItem, FileListItem>()

    private val overlayMenu = SolidOverlaySelectorMenu(SolidOverlayFileList(storage,focFactory))


    private var logCounter = 0
    private fun log() {
        logCounter++
        if (logCounter > 30) {
            AppLog.d(this, "Refs (items): ${items.size}")
            logCounter = 0
        }
    }

    init {
        try {
            listIndex.size = iteratorSimple.count

            val factory = SignalListItemFactory()
            factory.onSetup { item: ListItem ->
                items[item] = FileListItem(item, FileContextMenu(overlayMenu, iteratorSimple, uiController, application), descriptions)
                log()
            }

            factory.onBind { item: ListItem ->
                val index = ListIndex.toIndex(item)
                iteratorSimple.moveToPosition(index)
                items[item]?.bind(iteratorSimple.info, index)
            }

            factory.onTeardown { item: ListItem->
                items.remove(item)
                log()
            }

            val list = ListView(listIndex.inSelectionModel(), factory)
            list.onActivate {
                iteratorSimple.moveToPosition(it)
                uiController.load(iteratorSimple.info)
            }

            iteratorSimple.setOnCursorChangedListener {
                if (listIndex.size != iteratorSimple.count) {
                    listIndex.size = iteratorSimple.count
                }
            }

            val scrolled = ScrolledWindow()
            scrolled.child = list
            scrolled.hexpand = true
            scrolled.vexpand = true

            vbox.append(SolidDirectoryQueryComboView(storage, focFactory).combo)
            vbox.append(scrolled)
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }
}
