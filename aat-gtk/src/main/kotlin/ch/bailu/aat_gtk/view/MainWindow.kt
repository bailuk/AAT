package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.app.TimeStation
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.icons.IconMap
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.view.menu.provider.AppMenu
import ch.bailu.aat_lib.dispatcher.*
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.lib.bridge.CSS
import ch.bailu.gtk.type.Str

class MainWindow(window: ApplicationWindow, app: Application, dispatcher: Dispatcher)
{
    private val trackerButton = TrackerButtonStartPauseResume(GtkAppContext.services)

    private val contextRevealButton = ToggleButton()
    private val mainView = MainStackView(app, dispatcher,window, GtkAppContext.storage, contextRevealButton)
    private val contextBar = ContextBar(mainView,GtkAppContext.storage)

    private val box = Box(Orientation.VERTICAL, 0)


    init {
        TimeStation.log("init")


        box.append(contextBar.revealer)
        box.append(mainView.widget)

        window.setIconName(Strings.appId)
        window.child = box
        window.title = Str(GtkAppConfig.title)
        window.titlebar = createHeader(window, app,dispatcher, mainView)

        window.setDefaultSize(Layout.windowWidth, Layout.windowHeight)
        TimeStation.log("-> window.show")
        window.show()

        dispatcher.addSource(TrackerTimerSource(GtkAppContext.services, GtkTimer()))
        dispatcher.addSource(CurrentLocationSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(TrackerSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(OverlaySource(GtkAppContext))

        dispatcher.addTarget(trackerButton, InfoID.ALL)
        dispatcher.addTarget(contextBar, InfoID.ALL)

        CSS.addProviderForDisplay(window.display, Strings.appCss)
        window.onDestroy {
            mainView.onDestroy()
            App.exit(0)
        }
    }


    private fun createHeader(window: ApplicationWindow, app: Application, dispatcher: Dispatcher, stack: MainStackView): HeaderBar {
        val header = HeaderBar()

        header.showTitleButtons = true

        contextRevealButton.child = IconMap.getImage("zoom-original", 24)
        contextRevealButton.onToggled {
            contextBar.revealer.revealChild = contextRevealButton.active
        }

        header.packStart(MenuButton().apply {
            val appMenu = AppMenu(window, GtkAppContext.services, dispatcher, stack)
            menuModel = appMenu.createMenu()
            appMenu.createActions(app) // TODO is this the right place?
            PopoverMenu(popover.cast()).apply {
                appMenu.createCustomWidgets().forEach {
                    addChild(it.widget, Str(it.id))
                }
            }
        })

        header.packStart(trackerButton.button)
        header.packEnd(contextRevealButton)

        return header
    }
}
