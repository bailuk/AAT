package ch.bailu.aat_gtk.ui.view.menu.model

abstract class Menu {
    private val items = ArrayList<Item>()

    fun forEach(function: (Item) -> Unit) {
        items.forEach(function)
    }

    fun add(item: Item) {
        items.add(item)
    }
}