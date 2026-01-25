package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.TrackerSplitButton
import ch.bailu.aat_gtk.view.dialog.About
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.lib.handler.CallbackHandler
import ch.bailu.gtk.lib.handler.SignalHandler
import ch.bailu.gtk.lib.handler.action.ActionHandler
import ch.bailu.gtk.type.Str

class AppMenu(private val window: Window,
              private val services: ServicesInterface,
              private val dispatcher: DispatcherInterface,
              private val uiController: UiControllerInterface
) :
    MenuProviderInterface {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendSection(Res.str().tracker(), Menu().apply {
                appendItem(MenuHelper.createCustomItem(Strings.CUSTOM_TRACKER_BUTTON))
            })
            appendSection(Str.NULL, Menu().apply {
                append(Res.str().intro_settings(), MenuHelper.toAppAction(Strings.ACTION_SHOW_SETTINGS))
                append("${Res.str().intro_about()}…", MenuHelper.toAppAction(Strings.ACTION_SHOW_ABOUT))
                append(ToDo.translate("Dump resources"), MenuHelper.toAppAction(Strings.ACTION_DUMP_RESOURCES))
            })
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val trackerButton = TrackerSplitButton(services, dispatcher)
        return arrayOf(CustomWidget(trackerButton.button, Strings.CUSTOM_TRACKER_BUTTON) {})
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, Strings.ACTION_SHOW_SETTINGS) { uiController.showPreferences() }
        MenuHelper.setAction(app, Strings.ACTION_SHOW_ABOUT) { About.show(window) }
        MenuHelper.setAction(app, Strings.ACTION_DUMP_RESOURCES) {
            CallbackHandler.dump(System.out)
            SignalHandler.dump(System.out)
            ActionHandler.dump(System.out)
        }
    }

    override fun updateActionValues(app: Application) {}
}
