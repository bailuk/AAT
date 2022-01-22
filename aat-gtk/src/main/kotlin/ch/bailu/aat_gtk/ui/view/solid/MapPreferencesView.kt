package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.solid.SolidGtkTileCacheDirectory
import ch.bailu.aat_gtk.ui.view.VerticalScrollView
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gtk.Window

class MapPreferencesView (storage: StorageInterface, window: Window) : VerticalScrollView() {
    init {
        add(Res.str().p_map())
        add(SolidIndexComboView(SolidTileSize(storage, GtkAppConfig.density)).layout)
        add(SolidDirectorySelectorView(SolidGtkTileCacheDirectory(storage, GtkAppConfig.Foc) , window).layout)

        add(Res.str().p_offline_map())
    }
}