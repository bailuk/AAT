package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.menu.provider.CustomWidget
import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.aat_lib.resources.Res

class LocationMenu : MenuProvider {
    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .label(Res.str().location_send()) { println(Res.str().location_send()) }
            .label(Res.str().location_view()) { println(Res.str().location_view()) }
            .label(Res.str().clipboard_copy()) { println(Res.str().clipboard_copy()) }
            .label(Res.str().clipboard_paste()) { println(Res.str().clipboard_paste()) }
            .label(Res.str().p_goto_location()) { println(Res.str().p_goto_location()) }

    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }
}
