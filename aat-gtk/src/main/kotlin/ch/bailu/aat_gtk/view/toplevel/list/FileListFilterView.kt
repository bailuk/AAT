package ch.bailu.aat_gtk.view.toplevel.list

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.menu.PopupMenuButton
import ch.bailu.aat_gtk.view.menu.provider.FilterListContextMenu
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.SearchEntry
import ch.bailu.gtk.gtk.Spinner
import ch.bailu.gtk.type.Str

class FileListFilterView(private val app: Application, appContext: AppContext, uiController: UiControllerInterface) {
    private val fileCountLabel = Label(Str.NULL)
    private val solidDirectoryQuery = SolidDirectoryQuery(appContext.storage, appContext)
    private val filterContextMenu = FilterListContextMenu(app.activeWindow, solidDirectoryQuery, uiController)
    private val filterListMenuButton = PopupMenuButton(filterContextMenu).apply { createActions(app) }.menuButton

    private val summaryFrameButton = Button().apply {
        iconName = Icons.zoomFitBestSymbolic
        onClicked {
            uiController.frameInMap(InfoID.LIST_SUMMARY)
            uiController.setOverlayEnabled(InfoID.LIST_SUMMARY, true)
            uiController.showMap()
        }
    }

    private val summaryCenterButton = Button().apply {
        iconName = Icons.findLocationSymbolic
        onClicked {
            uiController.centerInMap(InfoID.LIST_SUMMARY)
            uiController.setOverlayEnabled(InfoID.LIST_SUMMARY, true)
            uiController.showMap()
        }
    }

    private val summaryDetailButton = Button().apply {
        iconName = Icons.viewContinuousSymbolic
        onClicked {
            uiController.showInDetail(InfoID.LIST_SUMMARY)
            uiController.showDetail()
        }
    }

    private val filterEntry = SearchEntry().apply {
        asEditable().onChanged {
            solidDirectoryQuery.solidNameFilter.setValue(asEditable().text.toString())
        }
    }

    val box = Box(Orientation.HORIZONTAL, Layout.MARGIN).apply {
        append(Box(Orientation.HORIZONTAL, 0).apply {
            append(filterEntry)
            append(summaryFrameButton)
            append(summaryCenterButton)
            append(summaryDetailButton)
            append(filterListMenuButton)
            addCssClass(Strings.CSS_LINKED)
        })

        append(Spinner().apply {
            appContext.broadcaster.register(AppBroadcaster.DBSYNC_DONE) { stop() }
            appContext.broadcaster.register(AppBroadcaster.DB_SYNC_CHANGED) { start() }
            appContext.broadcaster.register(AppBroadcaster.DBSYNC_START) { start() }
        })
        append(fileCountLabel.apply {
            hexpand = true
        })
    }

    init {
        solidDirectoryQuery.register { _, key ->
            if (solidDirectoryQuery.hasKey(key)) {
                updateFilterEntry()
                updateHighlighted()
                updateMenu()
            } else if (solidDirectoryQuery.containsKey(key)) {
                updateHighlighted()
            }
        }
        updateHighlighted()
        updateFilterEntry()
    }

    private fun updateMenu() {
        filterContextMenu.updateActionValues(app)
    }

    private fun updateFilterEntry() {
        filterEntry.asEditable().setText(solidDirectoryQuery.solidNameFilter.getValueAsStringNonDef().trim())
    }

    fun updateFileCount(count: Int) {
        fileCountLabel.setLabel(count.toString())
    }

    private fun updateHighlighted() {
        if (solidDirectoryQuery.isFilterEnabled()) {
            fileCountLabel.addCssClass(Strings.CSS_HIGHLIGHTED)
        } else {
            fileCountLabel.removeCssClass(Strings.CSS_HIGHLIGHTED)
        }
    }
}
