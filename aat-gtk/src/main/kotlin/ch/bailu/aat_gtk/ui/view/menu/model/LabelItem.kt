package ch.bailu.aat_gtk.ui.view.menu.model

open class LabelItem(val label: String, val onSelect: (Boolean) -> Unit) : Item()