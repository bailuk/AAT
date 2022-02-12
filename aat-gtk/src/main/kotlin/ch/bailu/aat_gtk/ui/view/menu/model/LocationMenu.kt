package ch.bailu.aat_gtk.ui.view.menu.model

import ch.bailu.aat_lib.resources.Res

class LocationMenu : Menu() {
    init {
        add(LabelItem(Res.str().location_send()) {println(Res.str().location_send())})
        add(LabelItem(Res.str().location_view()) {println(Res.str().location_view())})
        add(LabelItem(Res.str().clipboard_copy()) {println(Res.str().clipboard_copy())})
        add(LabelItem(Res.str().clipboard_paste()) {println(Res.str().clipboard_paste())})
        add(LabelItem(Res.str().p_goto_location()) {println(Res.str().p_goto_location())})
    }
}