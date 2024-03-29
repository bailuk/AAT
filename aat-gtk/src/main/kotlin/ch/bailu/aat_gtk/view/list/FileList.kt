package ch.bailu.aat_gtk.view.list

import ch.bailu.aat_gtk.app.GtkAppContext
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
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.foc.FocFactory
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
import ch.bailu.gtk.lib.bridge.ListIndex
import ch.bailu.gtk.lib.util.SizeLog
import ch.bailu.gtk.type.Str

class FileList(app: Application,
               storage: StorageInterface,
               focFactory: FocFactory,
               private val uiController: UiController
) {
    private val descriptions = arrayOf(
        DateDescription(),
        DistanceDescription(GtkAppContext.storage),
        AverageSpeedDescription(GtkAppContext.storage),
        TimeDescription())


    val vbox = Box(Orientation.VERTICAL, Layout.margin).apply {
        margin(Layout.margin)
    }

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

    private val iteratorSimple = IteratorSimple(GtkAppContext).apply {
        setOnCursorChangedListener {
            if (listIndex.size != count) {
                if (!listIsDirty) {
                    listIsDirty = true
                    updateLater()
                }
            }
        }
    }

    private fun updateLater() {
        updateTimer.kick(1000) {
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
        SolidCustomOverlayList(
            storage,
            focFactory
        ), SolidMockLocationFile(storage)).apply {
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
        select(-1)
        try {
            listIndex.size = iteratorSimple.count

            val factory = SignalListItemFactory().apply {
                onSetup {
                    val item = ListItem(it.cast())

                    items[item] = FileListItem(item, descriptions)
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

            vbox.append(fileNameLabel)
            vbox.append(Box(Orientation.HORIZONTAL, Layout.margin).apply {
                append(Box(Orientation.HORIZONTAL, 0).apply {
                    addCssClass(Strings.linked)
                    append(
                        SolidDirectoryQueryComboView(
                            storage,
                            focFactory
                        ).combo.apply { addCssClass(Strings.linked) })
                    append(Button().apply {
                        iconName = Icons.folderSymbolic
                        onClicked {
                            val path = SolidDirectoryQuery(storage, focFactory).valueAsFile.path
                            Directory.openExternal(path)
                        }
                    })
                })

                append(Separator(Orientation.VERTICAL))

                append(Box(Orientation.HORIZONTAL, 0).apply {
                    append(trackFrameButton)
                    append(trackCenterButton)
                    append(trackDetailButton)
                    append(menuButton)
                    addCssClass(Strings.linked)
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
