package ch.bailu.aat_gtk.view.menu.model

import java.util.*
import kotlin.collections.ArrayList


data class Group(val id: String = UUID.randomUUID().toString())


abstract class Item {
    val id: String = UUID.randomUUID().toString()
}

abstract class CheckItem(val onSelected: (Item) -> Unit) : Item() {
    abstract val label : String
    abstract val selected : Boolean
}

abstract class RadioItem(val onSelected: (Item) -> Unit, val group: Group) : Item() {
    abstract val label : String
    abstract val selected : Boolean
}

abstract class LabelItem(val onSelected: (Item) -> Unit) : Item() {
    abstract val label : String
}

class FixedLabelItem(override val label: String, onSelected: (Item)->Unit) : LabelItem(onSelected)

class SeparatorItem(val menu: Menu) : Item()
class SubmenuItem(val menu: Menu) : Item()

open class Menu(val title: String = "") {
    private val items = ArrayList<Item>()

    fun forEach(function: (Item) -> Unit) {
        items.forEach(function)
    }

    fun add(item: Item) {
        items.add(item)
    }
}