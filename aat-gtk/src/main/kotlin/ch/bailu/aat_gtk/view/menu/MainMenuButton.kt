package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.menu.provider.AppMenu
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.gtk.gtk.ApplicationWindow

class MainMenuButton(window: ApplicationWindow, dispatcher: Dispatcher, uiController: UiControllerInterface):
    PopupMenuButton(AppMenu(window, GtkAppContext.services, dispatcher, uiController))
