package ch.bailu.aat_gtk.ui.window

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.ui.util.IconMap
import ch.bailu.aat_gtk.ui.view.ContextBar
import ch.bailu.aat_gtk.ui.view.MainStackView
import ch.bailu.aat_gtk.ui.view.TrackerButton
import ch.bailu.aat_gtk.ui.view.menu.AppMenu
import ch.bailu.aat_gtk.ui.view.menu.GtkMenu
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str

class MainWindow(actionHelper: ActionHelper, window: ApplicationWindow)
{

    private val trackerButton = TrackerButton(GtkAppContext.services)

    private val dispatcher = Dispatcher()

    private val mainView = MainStackView(actionHelper,dispatcher,window)
    private val contextBar = ContextBar(mainView)

    private val box = Box(Orientation.VERTICAL, 0)

    private val menu: AppMenu

    init {
        box.append(contextBar.revealer)
        box.append(mainView.layout)

        window.child = box

        menu = AppMenu(window, GtkAppContext.services, mainView)
        window.title = Str(GtkAppConfig.title)
        window.titlebar = createHeader(GtkMenu(actionHelper, menu).menu)

        window.setDefaultSize(720 / 2, 1440 / 2)
        window.onDestroy {
            App.exit(0)
        }
        window.show()

        dispatcher.addSource(CurrentLocationSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(TrackerSource(GtkAppContext.services, GtkAppContext.broadcaster))

        dispatcher.addTarget(trackerButton, InfoID.ALL)
        dispatcher.addTarget(contextBar, InfoID.FILEVIEW)

        dispatcher.onResume()
    }


    private fun createHeader(menu: Menu): HeaderBar {
        val header = HeaderBar()

        header.showTitleButtons = GTK.TRUE

        val contextRevealButton = ToggleButton()
        contextRevealButton.child = IconMap.getImage("zoom-original", 24)
        contextRevealButton.onToggled {
            contextBar.revealer.revealChild = contextRevealButton.active
        }

        val menuButton = MenuButton()
        menuButton.menuModel = menu

        header.packStart(menuButton)
        header.packStart(trackerButton.button)

        header.packEnd(contextRevealButton)

        return header
    }
}
