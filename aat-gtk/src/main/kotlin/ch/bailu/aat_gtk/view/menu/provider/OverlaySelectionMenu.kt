package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.OverlayController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Orientation

class OverlaySelectionMenu(private val overlays: List<OverlayController>) : MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem("overlay"))
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val checkButtons = ArrayList<CheckButton>()

        return arrayOf(
            CustomWidget(
                Box(Orientation.VERTICAL, Layout.margin).apply {
                    overlays.forEach { controller ->
                        append(Box(Orientation.HORIZONTAL, 0).apply {
                            addCssClass(Strings.linked)

                            val checkButton = CheckButton().apply {
                                onToggled {
                                    controller.setEnabled(active)
                                }
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
                                iconName = Icons.viewContinuousSymbolic
                                onClicked {
                                    checkButton.active = true
                                    controller.showInDetail()
                                }
                            })

                            append(checkButton)
                        })
                    }
                }
            , "overlay") {
                overlays.forEachIndexed { index, overlayController ->
                    checkButtons[index].setLabel(overlayController.getName())
                    checkButtons[index].active = overlayController.isEnabled()
                }
            }
        )
    }

    override fun createActions(app: Application) {}
}
