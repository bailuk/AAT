package ch.bailu.aat_gtk.view.menu.model

import ch.bailu.aat_lib.resources.Res

class LocationMenu : Menu() {
    init {
        add(FixedLabelItem(Res.str().location_send()) {println(Res.str().location_send())})
        add(FixedLabelItem(Res.str().location_view()) {println(Res.str().location_view())})
        add(FixedLabelItem(Res.str().clipboard_copy()) {println(Res.str().clipboard_copy())})
        add(FixedLabelItem(Res.str().clipboard_paste()) {println(Res.str().clipboard_paste())})
        add(FixedLabelItem(Res.str().p_goto_location()) {println(Res.str().p_goto_location())})
    }
}