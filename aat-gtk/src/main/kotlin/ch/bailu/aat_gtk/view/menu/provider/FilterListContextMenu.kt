package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.view.dialog.CalendarDialog
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.SolidTypeInterface
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.lib.handler.action.ActionHandler
import ch.bailu.gtk.type.Str

class FilterListContextMenu(private val parent: Widget,
                            private val solidDirectoryQuery: SolidDirectoryQuery,
                            private val uiControllerInterface: UiControllerInterface)
    : MenuProviderInterface {
    override fun createMenu(): Menu {
        return Menu().apply {
            val solidSortAttribute = solidDirectoryQuery.solidSortAttribute
            val solidSortOrder = solidDirectoryQuery.solidSortOrderAscend
            appendSection(solidSortAttribute.getLabel(), Menu().apply {
                solidSortAttribute.getStringArray().forEachIndexed { index, value ->
                    append(value, MenuHelper.toAppAction(ACTION_SORT_ATTRIBUTE, index))
                }
            })

            appendSection(Str.NULL, Menu().apply {
                append(solidSortOrder.getLabel(), MenuHelper.toAppAction(ACTION_SORT_ASCEND))
            })
            appendSection(Res.str().label_filter(), Menu().apply {
                appendItem(MenuHelper.createCustomItem(CUSTOM_SORT_FILTER))
            })
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val widgets = arrayOf(
            SolidFilterWidget( {solidDirectoryQuery.useGeo}, {solidDirectoryQuery.boundingBox}, { self ->
                solidDirectoryQuery.boundingBox.value = uiControllerInterface.getMapBounding()
                self.update()
            }),
            SolidFilterWidget({solidDirectoryQuery.useDateStart}, {solidDirectoryQuery.dateStart}, { self ->
                CalendarDialog.getDate(parent, solidDirectoryQuery.dateStart.getValue()) {
                    solidDirectoryQuery.dateStart.setValue(it)
                    self.update()
                }
            }),
            SolidFilterWidget({solidDirectoryQuery.useDateEnd}, {solidDirectoryQuery.dateEnd}, { self ->
                CalendarDialog.getDate(parent, solidDirectoryQuery.dateEnd.getValue()) {
                    solidDirectoryQuery.dateEnd.setValue(it)
                    self.update()
                }
            })
        )

        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    widgets.forEach { append(it.box) }
                    onRowActivated { id -> widgets[id.index].onClicked() }
                },
                CUSTOM_SORT_FILTER,
                {
                    widgets.forEach { it.update() }
                }
            )
        )
    }

    private class SolidFilterWidget(private val solidCheckbox: () -> SolidBoolean,
                            private val solidValue: () -> SolidTypeInterface,
                            private val onClickedCallback: (self: SolidFilterWidget)->Unit) {
        private val checkButton = CheckButton().apply {
            onToggled { solidCheckbox().value = active }
        }
        private val label = Label(Str.NULL).apply { xalign = 0f }
        private val value = Label(Str.NULL).apply { xalign = 0f }
        val box = Box(Orientation.HORIZONTAL, 0).apply {
            append(checkButton)
            append(Box(Orientation.VERTICAL, 0).apply {
                append(label)
                append(value)
            })
            margin(Layout.MARGIN)
        }

        fun update() {
            checkButton.active = solidCheckbox().value
            label.setLabel(solidValue().getLabel())
            value.setLabel(solidValue().getValueAsString())
        }

        fun onClicked() {
            onClickedCallback(this)
        }

        init {
            update()
        }
    }

    override fun createActions(app: Application) {
        ActionHandler.get(app, ACTION_SORT_ATTRIBUTE, solidDirectoryQuery.solidSortAttribute.index).onChange { value ->
            solidDirectoryQuery.solidSortAttribute.index = value
        }
        ActionHandler.get(app, ACTION_SORT_ASCEND, solidDirectoryQuery.solidSortOrderAscend.value).onToggle { value ->
            solidDirectoryQuery.solidSortOrderAscend.value = value
        }
    }

    override fun updateActionValues(app: Application) {
        ActionHandler
            .get(app, ACTION_SORT_ATTRIBUTE, solidDirectoryQuery.solidSortAttribute.index)
            .changeInteger(solidDirectoryQuery.solidSortAttribute.index)

        ActionHandler
            .get(app, ACTION_SORT_ASCEND, solidDirectoryQuery.solidSortOrderAscend.value)
            .changeBoolean(solidDirectoryQuery.solidSortOrderAscend.value)
    }

    companion object {
        const val ACTION_SORT_ASCEND   = "sortAscend"
        const val ACTION_SORT_ATTRIBUTE = "sortAttribute"
        const val CUSTOM_SORT_FILTER = "sortFilter"
    }
}
