package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.util.IconMap
import ch.bailu.aat_gtk.view.menu.AppMenu
import ch.bailu.aat_gtk.view.menu.NewPopupButton
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.dispatcher.TrackerTimerSource
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str

class MainWindow(actionHelper: ActionHelper, window: ApplicationWindow, app: Application)
{

    private val trackerButton = TrackerButton(GtkAppContext.services)

    private val dispatcher = Dispatcher()

    private val mainView = MainStackView(actionHelper,dispatcher,window)
    private val contextBar = ContextBar(mainView)

    private val box = Box(Orientation.VERTICAL, 0)


    init {
        box.append(contextBar.revealer)
        box.append(mainView.layout)

        window.child = box

        window.title = Str(GtkAppConfig.title)
        window.titlebar = createHeader(window, app, mainView)

        window.setDefaultSize(720 / 2, 1440 / 2)
        window.onDestroy {
            App.exit(0)
        }
        window.show()

        dispatcher.addSource(TrackerTimerSource(GtkAppContext.services, GtkTimer()))
        dispatcher.addSource(CurrentLocationSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(TrackerSource(GtkAppContext.services, GtkAppContext.broadcaster))

        dispatcher.addTarget(trackerButton, InfoID.ALL)
        dispatcher.addTarget(contextBar, InfoID.FILEVIEW)

        dispatcher.onResume()
    }


    private fun createHeader(window: ApplicationWindow, app: Application, stack: MainStackView): HeaderBar {
        val header = HeaderBar()

        header.showTitleButtons = GTK.TRUE

        val contextRevealButton = ToggleButton()
        contextRevealButton.child = IconMap.getImage("zoom-original", 24)
        contextRevealButton.onToggled {
            contextBar.revealer.revealChild = contextRevealButton.active
        }

        val menuProvider = AppMenu(app, window, stack)
        val menuButton = NewPopupButton(menuProvider.createMenu(), menuProvider.createCustomWidgets())
        menuButton.setIcon("menu", 12)

        header.packStart(menuButton.overlay)
        header.packStart(trackerButton.button)

        header.packEnd(contextRevealButton)

        return header
    }
}
