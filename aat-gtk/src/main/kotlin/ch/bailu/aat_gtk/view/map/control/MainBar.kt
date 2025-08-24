package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.search.SearchController
import ch.bailu.aat_gtk.view.toplevel.navigation.NavigationView
import ch.bailu.aat_gtk.view.toplevel.navigation.NavigationViewChanged
import ch.bailu.aat_gtk.view.tracker.TrackerControllerView
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.file.json.SearchModel
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.extensions.ellipsize
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Separator
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.lib.handler.action.ActionHandler

class MainBar(private val app: Application, uiController: UiControllerInterface, services: ServicesInterface, dispatcher: DispatcherInterface) : Bar(Position.TOP),
    NavigationViewChanged {
    private val searchModel = SearchModel()
    private val searchController = SearchController(searchModel)
    private val trackerControllerView = TrackerControllerView(services, dispatcher, uiController)
    private val leftNavigationButton = Button().apply {
        addCssClass(Strings.mapControl)
        iconName = Icons.goPreviousSymbolic
        onClicked {
            uiController.hideMap()
        }
    }

    private val rightNavigationButton = Button().apply {
        setLabel("POI")
        onClicked {
            uiController.showPoi()
        }
    }

    private val leftSeparator = Separator(Orientation.VERTICAL)
    private val rightSeparator = Separator(Orientation.VERTICAL)

    init {
        searchController.centerMap = { pos ->
            uiController.centerInMap(pos)
        }

        add(Box(Orientation.VERTICAL, Layout.MARGIN).apply {
            append(Box(Orientation.HORIZONTAL, Layout.MARGIN).apply {
                append(leftNavigationButton)
                append(leftSeparator)
                append(createNominatimWidget())
                append(rightSeparator)
                append(rightNavigationButton)
            })
            append(trackerControllerView.box)
        })
    }

    override fun onNavigationViewChanged(navigationView: NavigationView) {
        leftSeparator.visible = navigationView.leftCollapsed
        leftNavigationButton.visible = navigationView.leftCollapsed
        trackerControllerView.box.visible = navigationView.leftCollapsed
        rightSeparator.visible = navigationView.rightCollapsed
        rightNavigationButton.visible = navigationView.rightCollapsed
    }

    private fun createMenuModel(searchModel: SearchModel): Menu {
        val menu = Menu()
        searchModel.forEachIndexed { index, name, latLong ->
            menu.append(name.ellipsize(), "app.slot$index")
            ActionHandler.get(app, "slot$index").apply {
                disconnectSignals()
                onActivate { _ -> searchController.centerMap(latLong) }
            }
        }
        return menu
    }

    private fun createNominatimWidget(): Widget {
        return Box(Orientation.HORIZONTAL, 0).apply {
            addCssClass(Strings.linked)

            val entry = Entry()
            append(entry.apply {
                onActivate {
                    // When 'enter' was pressed
                    searchController.search(asEditable().text.toString())
                }
            })

            append(Button().apply {
                iconName = Icons.editFindSymbolic
                onClicked {
                    searchController.search(entry.asEditable().text.toString())
                }
            })

            append(MenuButton().apply {
                iconName = Icons.viewMoreSymbolic
                searchModel.observe {
                    menuModel = createMenuModel(it)
                }
            })
        }
    }
}
