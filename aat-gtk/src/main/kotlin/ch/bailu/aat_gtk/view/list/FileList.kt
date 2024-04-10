package ch.bailu.aat_gtk.view.list

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.util.Directory
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_gtk.view.menu.provider.FileContextMenu
import ch.bailu.aat_gtk.view.solid.SolidDirectoryQueryComboView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListItem
import ch.bailu.gtk.gtk.ListView
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.gtk.ScrolledWindow
import ch.bailu.gtk.gtk.Separator
import ch.bailu.gtk.gtk.SignalListItemFactory
import ch.bailu.gtk.gtk.Spinner
import ch.bailu.gtk.lib.bridge.ListIndex
import ch.bailu.gtk.lib.util.SizeLog
import ch.bailu.gtk.type.Str

class FileList(app: Application,
               appContext: AppContext,
               private val uiController: UiController
) {
    private val descriptions = arrayOf(
        DateDescription(),
        DistanceDescription(appContext.storage),
        AverageSpeedDescription(appContext.storage),
        TimeDescription())

    val vbox = Box(Orientation.VERTICAL, Layout.margin).apply {
        margin(Layout.margin)
    }

    private val fileCountLabel = Label(Str.NULL)
    private val fileNameLabel = Label(Str.NULL)
    private val trackFrameButton = Button().apply {
        iconName = Icons.zoomFitBestSymbolic
        onClicked { selectAndFrame(indexOfSelected) }
    }

    private val trackCenterButton = Button().apply {
        iconName = Icons.findLocationSymbolic
        onClicked { selectAndCenter(indexOfSelected) }
    }

    private val trackDetailButton = Button().apply {
        iconName = Icons.viewContinuousSymbolic
        onClicked { selectAndDetail(indexOfSelected) }
    }

    private val listIndex = ListIndex()

    private var listIsDirty = false
    private val updateTimer = GtkTimer()

    private val iteratorSimple = IteratorSimple(appContext).apply {
        setOnCursorChangedListener {
            updateList()
        }
    }

    private fun updateList() {
        fileCountLabel.setLabel(iteratorSimple.count.toString())
        if (listIndex.size != iteratorSimple.count) {
            if (!listIsDirty) {
                listIsDirty = true
                updateLater()
            }
        }
    }

    private fun updateLater() {
        updateTimer.kick(500) {
            if (listIsDirty) {
                listIsDirty = false
                val count = iteratorSimple.count
                AppLog.d(this, "Update list, new size: $count")
                listIndex.size = count
                if (count == 0) {
                    select(-1)
                }
            }
        }
    }

    private var indexOfSelected = -1

    private val items = HashMap<ListItem, FileListItem>()
    private val overlayMenu = FileContextMenu(
        appContext,
        SolidCustomOverlayList(
            appContext.storage,
            appContext
        ), SolidMockLocationFile(appContext.storage)).apply {
        createActions(app)
    }
    private val logItems = SizeLog("FileListItem")

    // TODO: reuse PopupButton?
    private val menuButton = MenuButton().apply {
        menuModel = overlayMenu.createMenu()
        PopoverMenu(popover.cast()).apply {
            val customWidgets = overlayMenu.createCustomWidgets()

            customWidgets.forEach {
                addChild(it.widget, it.id)
            }

            onShow {
                customWidgets.forEach {
                    it.update()
                }
            }
        }
    }

    init {
        try {
            select(-1)
            updateList()

            val factory = SignalListItemFactory().apply {
                onSetup {
                    val item = ListItem(it.cast())

                    items[item] = FileListItem(appContext, item, descriptions)
                    logItems.log(items.size.toLong())
                }

                onBind {
                    val item = ListItem(it.cast())
                    val index = ListIndex.toIndex(item)

                    iteratorSimple.moveToPosition(index)
                    items[item]?.bind(iteratorSimple.info, index)
                    if (item.selected) {
                        select(index)
                    }
                }

                onTeardown {
                    val item = ListItem(it.cast())
                    items.remove(item)
                    logItems.log(items.size.toLong())
                }
            }

            vbox.append(Box(Orientation.HORIZONTAL, Layout.margin).apply {
                append(Box(Orientation.HORIZONTAL, 0).apply {
                    addCssClass(Strings.linked)
                    append(
                        SolidDirectoryQueryComboView(appContext).combo.apply {
                            hexpand = true
                        })
                    append(Button().apply {
                        iconName = Icons.folderSymbolic
                        onClicked {
                            val path = SolidDirectoryQuery(appContext.storage, appContext).getValueAsFile().path
                            Directory.openExternal(path)
                        }
                    })
                })
                append(Spinner().apply {
                    appContext.broadcaster.register(AppBroadcaster.DBSYNC_DONE) { stop() }
                    appContext.broadcaster.register(AppBroadcaster.DB_SYNC_CHANGED) { start() }
                    appContext.broadcaster.register(AppBroadcaster.DBSYNC_START) { start() }
                })
                append(fileCountLabel)
            })

            vbox.append(Box(Orientation.HORIZONTAL, Layout.margin).apply {
                append(Box(Orientation.HORIZONTAL, 0).apply {
                    append(trackFrameButton)
                    append(trackCenterButton)
                    append(trackDetailButton)
                    append(menuButton)
                    addCssClass(Strings.linked)
                })
                append(Separator(Orientation.VERTICAL))
                append(fileNameLabel)
            })

            vbox.append(ScrolledWindow().apply {
                child = ListView(listIndex.inSelectionModel(), factory).apply {
                    onActivate { selectAndFrame(it) }
                }
                hexpand = true
                vexpand = true
            })


            MenuHelper.setAction(app, Strings.actionFileEdit) {
                selectAndEdit(indexOfSelected)
            }

        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }


    private fun selectAndFrame(index: Int) {
        select(index)
        if (isIndexValid(indexOfSelected)) {
            uiController.load(iteratorSimple.info)
            uiController.showMap()
            uiController.frameInMap(iteratorSimple.info)
        }
    }

    private fun selectAndEdit(index: Int) {
        select(index)
        if (isIndexValid(indexOfSelected)) {
            uiController.load(iteratorSimple.info)
            uiController.showMap()
            uiController.frameInMap(iteratorSimple.info)
            uiController.loadIntoEditor(iteratorSimple.info)
        }
    }

    private fun selectAndCenter(index: Int) {
        select(index)
        if (isIndexValid(indexOfSelected)) {
            uiController.load(iteratorSimple.info)
            uiController.showMap()
            uiController.centerInMap(iteratorSimple.info)
        }
    }

    private fun selectAndDetail(index: Int) {
        select(index)
        if (isIndexValid(indexOfSelected)) {
            uiController.load(iteratorSimple.info)
            uiController.showDetail()
            uiController.showInDetail(InfoID.FILE_VIEW)
        }
    }

    private fun select(index: Int) {
        indexOfSelected = index

        if (isIndexValid(indexOfSelected)) {
            iteratorSimple.moveToPosition(indexOfSelected)
            overlayMenu.setFile(iteratorSimple.info.file)
            fileNameLabel.setLabel(iteratorSimple.info.file.name)
            menuButton.sensitive = true
        } else {
            fileNameLabel.label = Str.NULL
            menuButton.sensitive = false
        }
    }

    private fun isIndexValid(index: Int): Boolean {
        return index > -1 && index < iteratorSimple.count
    }
}
