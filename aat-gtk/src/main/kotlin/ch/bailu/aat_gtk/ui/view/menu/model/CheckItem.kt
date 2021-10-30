package ch.bailu.aat_gtk.ui.view.menu.model

class CheckItem(label: String, var selected: Boolean,
                onSelect: (Item) -> Unit
) : LabelItem(label, onSelect) {
}