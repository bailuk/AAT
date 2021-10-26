package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class AppMenu (window: ApplicationWindow, spos: SolidPositionLock, sgrid: SolidMapGrid){
    val menu = Menu()

    private val map = MenuItem()
    private val cockpit = MenuItem()
    private val settings = MenuItem()
    private val tracks = MenuItem()

    private val pinePhoneLowRes: MenuItem
    private val pinePhoneHighRes: MenuItem

    init {

        map.label = Str("Map")
        menu.append(map)

        cockpit.label = Str("Cockpit")
        menu.append(cockpit)

        tracks.label = Str("Tracks & Overlays")
        menu.append(tracks)

        settings.label = Str("Preferences")
        menu.append(settings)

        val separator = SeparatorMenuItem()
        menu.append(separator)
        pinePhoneLowRes = MenuItem()
        pinePhoneLowRes.label = Str("PinePhone low res")
        pinePhoneLowRes.onActivate {window.resize(720 / 2, 1440 / 2)}
        menu.add(pinePhoneLowRes)

        pinePhoneHighRes = MenuItem()
        pinePhoneHighRes.label = Str("PinePhone high res")
        pinePhoneHighRes.onActivate {window.resize(720, 1440)}
        menu.add(pinePhoneHighRes)
        menu.showAll()
    }
}