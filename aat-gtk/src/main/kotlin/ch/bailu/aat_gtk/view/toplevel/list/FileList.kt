package ch.bailu.aat_gtk.view.toplevel.list

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.provider.SolidOverlaySelectorMenu
import ch.bailu.aat_gtk.view.solid.SolidDirectoryQueryComboView
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.*
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


    val vbox = Box(Orientation.VERTICAL, 0).apply {
        margin(3)
    }

    private val indexLabel = Label(Str.NULL).apply {
        hexpand = true
        halign = Align.END
        marginEnd = 3
    }

    private val loadButton = Button().apply {
        addCssClass(Strings.linked)
        setLabel(ToDo.translate("Load"))
        onClicked {
            selectAndLoad(indexOfSelected)
        }
    }

    private val listIndex = ListIndex()

    private val iteratorSimple = IteratorSimple(GtkAppContext).apply {
        setOnCursorChangedListener {
            if (listIndex.size != count) {
                listIndex.size = count
            }

            if (count == 0) {
                select(-1)
            }
        }
    }

    private var indexOfSelected = -1

    private val items = HashMap<ListItem, FileListItem>()
    private val overlayMenu = SolidOverlaySelectorMenu(SolidOverlayFileList(storage,focFactory), SolidMockLocationFile(storage)).apply {
        createActions(app)
    }
    private val logItems = SizeLog("FileListItem")


    private val menuButton = MenuButton().apply {
        menuModel = overlayMenu.createMenu()
        PopoverMenu(popover.cast()).apply {
            overlayMenu.createCustomWidgets().forEach {
                addChild(it.widget, it.id)
            }
        }
    }

    init {
        select(-1)
        try {
            listIndex.size = iteratorSimple.count

            val factory = SignalListItemFactory().apply {
                onSetup { item: ListItem ->
                    items[item] = FileListItem(item, descriptions)
                    logItems.log(items.size.toLong())
                }

                onBind { item: ListItem ->
                    val index = ListIndex.toIndex(item)
                    iteratorSimple.moveToPosition(index)
                    items[item]?.bind(iteratorSimple.info, index)
                    if (item.selected) {
                        select(index)
                    }
                }

                onTeardown { item: ListItem->
                    items.remove(item)
                    logItems.log(items.size.toLong())
                }
            }

            vbox.append(Box(Orientation.HORIZONTAL, 0).apply {
                append(SolidDirectoryQueryComboView(storage, focFactory).combo)

                append(indexLabel)
                append(Box(Orientation.HORIZONTAL, 0).apply {
                    append(loadButton)
                    append(menuButton)
                    addCssClass(Strings.linked)
                })
            })

            vbox.append(ScrolledWindow().apply {
                child = ListView(listIndex.inSelectionModel(), factory).apply {
                    onActivate { selectAndLoad(it) }
                }
                hexpand = true
                vexpand = true
            })
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }

    private fun selectAndLoad(index: Int) {
        select(index)
        if (isIndexValid(indexOfSelected)) {
            uiController.load(iteratorSimple.info)
        }
    }

    private fun select(index: Int) {
        indexOfSelected = index

        if (isIndexValid(indexOfSelected)) {
            indexLabel.setLabel("[$indexOfSelected]")
            iteratorSimple.moveToPosition(indexOfSelected)
            overlayMenu.setFile(iteratorSimple.info.file)
            loadButton.sensitive = true
            menuButton.sensitive = true
        } else {
            indexLabel.setLabel("")
            loadButton.sensitive = false
            menuButton.sensitive = false
        }
    }

    private fun isIndexValid(index: Int): Boolean {
        return index > -1 && index < iteratorSimple.count
    }
}
