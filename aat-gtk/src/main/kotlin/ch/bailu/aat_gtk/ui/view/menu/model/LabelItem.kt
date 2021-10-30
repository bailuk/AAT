package ch.bailu.aat_gtk.ui.view.menu.model

open class LabelItem(open val label: String, val onSelect: (Item) -> Unit) : Item()