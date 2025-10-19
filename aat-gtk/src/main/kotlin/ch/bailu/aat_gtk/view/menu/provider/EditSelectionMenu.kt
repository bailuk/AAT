package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.OverlayControllerInterface
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Orientation

class EditSelectionMenu(private val overlays: List<OverlayControllerInterface>) : MenuProviderInterface {
    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem(ID))
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val checkButtons = ArrayList<CheckButton>()

        return arrayOf(
            CustomWidget(
                Box(Orientation.VERTICAL, Layout.MARGIN).apply {
                    overlays.forEach { controller ->
                        append(Box(Orientation.HORIZONTAL, 0).apply {
                            addCssClass(Strings.CSS_LINKED)

                            val checkButton = CheckButton().apply {
                                setLabel(controller.getName())
                                active = controller.isEnabled()
                                onToggled {
                                    controller.setEnabled(active)
                                }
                                hexpand = true
                            }
                            checkButtons.add(checkButton)

                            append(Button().apply {
                                iconName = Icons.findLocationSymbolic
                                onClicked {
                                    checkButton.active = true
                                    controller.center()
                                }
                            })
                            append(Button().apply {
                                iconName = Icons.zoomFitBestSymbolic
                                onClicked {
                                    checkButton.active = true
                                    controller.frame()
                                }
                            })
                            append(Button().apply {
                                iconName = Icons.editSelectAllSymbolic
                                onClicked {
                                    checkButton.active = true
                                    controller.edit()
                                }
                            })
                            append(checkButton)
                        })
                    }
                }
                , ID) {
                overlays.forEachIndexed { index, overlayController ->
                    checkButtons[index].setLabel(overlayController.getName())
                    checkButtons[index].active = overlayController.isEnabled()
                }
            }
        )
    }

    override fun createActions(app: Application) {}
    override fun updateActionValues(app: Application) {}

    companion object {
        private const val ID = "editable"
    }
}
