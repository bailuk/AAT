package ch.bailu.aat_gtk.view.toplevel.list

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.util.Directory
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_gtk.view.menu.PopupMenuButton
import ch.bailu.aat_gtk.view.menu.provider.FileContextMenu
import ch.bailu.aat_gtk.view.solid.SolidDirectoryDropDownView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.gpx.information.InfoID
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
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.ScrolledWindow
import ch.bailu.gtk.gtk.SignalListItemFactory
import ch.bailu.gtk.gtk.Spinner
import ch.bailu.gtk.lib.bridge.ListIndex
import ch.bailu.gtk.lib.util.SizeLog
import ch.bailu.gtk.pango.EllipsizeMode
import ch.bailu.gtk.type.Str

class FileListPage(app: Application,
                   appContext: AppContext,
                   private val uiController: UiControllerInterface
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
    private val menuButton = PopupMenuButton(overlayMenu).apply { createActions(app) }.menuButton

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
                    items[item]?.apply { teardown() }
                    items.remove(item)
                    logItems.log(items.size.toLong())
                }
            }

            vbox.append(Box(Orientation.HORIZONTAL, Layout.margin).apply {
                append(Box(Orientation.HORIZONTAL, 0).apply {
                    addCssClass(Strings.linked)
                    append(
                        SolidDirectoryDropDownView(appContext).dropDown.apply {
                            hexpand = true
                        })
                    append(Button().apply {
                        iconName = Icons.folderSymbolic
                        onClicked {
                            val path = SolidDirectoryQuery(appContext.storage, appContext).getValueAsFile().path
                            Directory.openExternal(path)
                        }
                    })
                    append(Button().apply {
                        iconName = Icons.viewRefreshSymbolic
                        onClicked {
                            appContext.services.getDirectoryService().rescan()
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
                append(fileNameLabel.apply {
                    ellipsize = EllipsizeMode.START
                    addCssClass("page-label")
                })
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
            uiController.setOverlayEnabled(InfoID.FILE_VIEW, true)
        }
    }

    private fun selectAndEdit(index: Int) {
        select(index)
        if (isIndexValid(indexOfSelected)) {
            uiController.loadIntoEditor(iteratorSimple.info)
        }
    }

    private fun selectAndCenter(index: Int) {
        select(index)
        if (isIndexValid(indexOfSelected)) {
            uiController.load(iteratorSimple.info)
            uiController.showMap()
            uiController.centerInMap(iteratorSimple.info)
            uiController.setOverlayEnabled(InfoID.FILE_VIEW, true)
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
            overlayMenu.setFile(iteratorSimple.info.getFile())
            fileNameLabel.setLabel(iteratorSimple.info.getFile().name)
            fileNameLabel.setTooltipText(iteratorSimple.info.getFile().toString())
            menuButton.sensitive = true
        } else {
            fileNameLabel.label = Str.NULL
            fileNameLabel.tooltipText = Str.NULL
            menuButton.sensitive = false
        }
    }

    private fun isIndexValid(index: Int): Boolean {
        return index > -1 && index < iteratorSimple.count
    }
}
