package ch.bailu.aat.menus

import android.view.Menu
import android.view.MenuItem
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListEntry
import ch.bailu.aat.views.osm_features.OnSelected

class MapFeaturesMenu(
    private val element: MapFeaturesListEntry,
    private val onSelected: OnSelected
) : AbsMenu() {

    private val variants: ArrayList<String> = element.variants

    override fun inflate(menu: Menu) {
        val g = OnSelected.EDIT

        for ((i, v) in variants.withIndex()) {
            menu.add(g, i, Menu.NONE, v)
        }
    }

    override val title: String
        get() = ""

    override fun prepare(menu: Menu) {}

    override fun onItemClick(item: MenuItem): Boolean {
        onSelected.onSelected(element, OnSelected.EDIT, variants[item.itemId])
        return true
    }
}
