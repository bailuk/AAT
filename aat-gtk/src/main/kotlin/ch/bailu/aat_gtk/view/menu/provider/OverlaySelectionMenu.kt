package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.dispatcher.GpxInformationSource
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Orientation

class OverlaySelectionMenu(private val overlays: List<GpxInformationSource>, private val uiController: UiController) : MenuProvider {

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
                    overlays.forEach {
                        append(Box(Orientation.HORIZONTAL, 0).apply {
                            val checkButton = CheckButton().apply {
                                onToggled {
                                    it.setEnabled(active)
                                }
                                hexpand = true
                            }

                            checkButtons.add(checkButton)

                            append(checkButton)
                            append(Box(Orientation.HORIZONTAL, 0).apply {
                                addCssClass(Strings.linked)
                                append(Button().apply {
                                    iconName = Icons.findLocationSymbolic
                                    onClicked {
                                        checkButton.active = true
                                        uiController.centerInMap(it.getInfo())
                                    }
                                })
                                append(Button().apply {
                                    iconName = Icons.zoomFitBestSymbolic
                                    onClicked {
                                        checkButton.active = true
                                        uiController.frameInMap(it.getInfo())
                                    }
                                })

                                append(Button().apply {
                                    iconName = Icons.openMenuSymbolic
                                    onClicked {
                                        checkButton.active = true
                                        uiController.loadIntoEditor(it.getInfo())
                                    }
                                })
                            })

                        })
                    }
                }
            , "overlay") {
                overlays.forEachIndexed { index, fileSource ->
                    checkButtons[index].setLabel(fileSource.getInfo().getFile().name)
                    checkButtons[index].active = fileSource.isEnabled()
                }
            }
        )
    }

    override fun createActions(app: Application) {}
}
