package ch.bailu.aat.menus

import android.view.Menu
import android.view.MenuItem
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListItem
import ch.bailu.aat.views.osm.features.OnSelected

class MapFeaturesMenu(
    private val element: MapFeaturesListItem,
    private val onSelected: OnSelected
) : AbsMenu() {

    private val variants: ArrayList<String> = element.variants

    override fun inflate(menu: Menu) {
        val groupId = OnSelected.Action.Edit.ordinal

        for ((itemId, variantTitle) in variants.withIndex()) {
            menu.add(groupId, itemId, Menu.NONE, variantTitle)
        }
    }

    override val title: String
        get() = ""

    override fun prepare(menu: Menu) {}

    override fun onItemClick(item: MenuItem): Boolean {
        onSelected.onSelected(element, OnSelected.Action.Edit, variants[item.itemId])
        return true
    }
}
