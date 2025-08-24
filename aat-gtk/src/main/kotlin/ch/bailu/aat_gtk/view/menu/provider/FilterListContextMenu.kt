package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidTypeInterface
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
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

class FilterListContextMenu(private val solidDirectoryQuery: SolidDirectoryQuery) : MenuProvider {
    override fun createMenu(): Menu {
        return Menu().apply {

            val solidSortOrder = solidDirectoryQuery.solidSortAttribute
            appendSection(solidSortOrder.getLabel(), Menu().apply {
                solidSortOrder.getStringArray().forEachIndexed { index, value ->
                    append(value, "app.sort($index)")
                }
            })

            appendSection(Str.NULL, Menu().apply {
                append(ToDo.translate("Descend"), "app.sort_descent")
            })
            appendSection(Res.str().label_filter(), Menu().apply {
                appendItem(MenuHelper.createCustomItem("SORT_FILTER"))
            })

        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    append(solidWidget(solidDirectoryQuery.boundingBox))
                    append(solidWidget(solidDirectoryQuery.dateStart))
                    append(solidWidget(solidDirectoryQuery.dateTo))
                    onRowActivated { id ->
                        AppLog.d(this, "row activated $id") }
                },
                "SORT_FILTER",
                {

                }
            )
        )
    }

    private fun solidWidget(solidTypeInterface: SolidTypeInterface): Widget {
        return Box(Orientation.HORIZONTAL, 0).apply {
            append(CheckButton())
            append(Box(Orientation.VERTICAL, 0).apply {
                append(Label(solidTypeInterface.getLabel()).apply {
                    xalign = 0f
                })
                append(Label("[${solidTypeInterface.getValueAsString()}]").apply {
                    xalign = 0f
                })
            })
            margin(Layout.MARGIN)
        }
    }
    override fun createActions(app: Application) {
        ActionHandler.get(app, "sort", 0).onChange { value ->
            AppLog.d(this, value.toString())
        }
        ActionHandler.get(app, "sort_descent", false).onToggle { value ->
            AppLog.d(this, value.toString())
        }
    }
}
