package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.dispatcher.FileSourceInterface
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Orientation

class OverlaySelectionMenu(private val overlays: List<FileSourceInterface>, private val uiController: UiController) : MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem("overlay"))
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                Box(Orientation.VERTICAL, Layout.margin).apply {
                    overlays.forEach {
                        append(Box(Orientation.HORIZONTAL, 0).apply {
                            addCssClass(Strings.linked)

                            val checkButton = CheckButton().apply {
                                setLabel(it.info.file.name)
                                active = it.isEnabled
                                onToggled {
                                    it.isEnabled = active
                                }
                            }

                            append(Button().apply {
                                setIconName("find-location-symbolic")
                                onClicked {
                                    checkButton.active = true
                                    uiController.centerInMap(it.info)
                                }
                            })
                            append(Button().apply {
                                setIconName("zoom-fit-best-symbolic")
                                onClicked {
                                    checkButton.active = true
                                    uiController.frameInMap(it.info)
                                }
                            })
                            append(checkButton)
                        })
                    }
                }
            , "overlay")
        )
    }

    override fun createActions(app: Application) {}
}
