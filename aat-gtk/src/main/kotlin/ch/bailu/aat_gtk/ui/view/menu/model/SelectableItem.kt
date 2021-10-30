package ch.bailu.aat_gtk.ui.view.menu.model

abstract class SelectableItem(label: String, onSelect: (Boolean) -> Unit) : LabelItem(label, onSelect) {
    abstract var selected: Boolean
}