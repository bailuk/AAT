package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.provider.AppMenu
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

class MainMenuButton(app: Application, window: ApplicationWindow, dispatcher: Dispatcher, uiController: UiController) {
    val menuButton = MenuButton().apply {
        val appMenu = AppMenu(window, GtkAppContext.services, dispatcher, uiController)
        menuModel = appMenu.createMenu()
        appMenu.createActions(app)
        PopoverMenu(popover.cast()).apply {
            appMenu.createCustomWidgets().forEach {
                addChild(it.widget, Str(it.id))
            }
        }
    }
}
