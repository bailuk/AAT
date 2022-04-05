package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MapMenu
import ch.bailu.aat_gtk.view.menu.MapQueryMenu
import ch.bailu.aat_gtk.view.menu.PopupButton
import ch.bailu.aat_gtk.view.menu.model.LocationMenu
import ch.bailu.aat_gtk.view.solid.SolidMenuButton
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidLegend
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.helper.ActionHelper

class InfoBar(actionHelper: ActionHelper, mcontext: MapContext, storage: StorageInterface, uiController: UiController) {

    val bar = Bar(Orientation.VERTICAL).apply {

        add(PopupButton(actionHelper, MapMenu(storage, GtkAppContext, uiController, mcontext)).apply { setIcon("menu") }.overlay)
        add(SolidMenuButton(actionHelper, SolidMapGrid(storage, mcontext.solidKey)).overlay)
        add(SolidMenuButton(actionHelper, SolidLegend(storage, mcontext.solidKey)).overlay)
        add(PopupButton(actionHelper, MapQueryMenu()).apply { setIcon("search") }.overlay)
        add(PopupButton(actionHelper, LocationMenu()).apply { setIcon("location") }.overlay)
    }
}
