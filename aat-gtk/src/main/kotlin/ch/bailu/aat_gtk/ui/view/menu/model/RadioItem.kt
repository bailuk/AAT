package ch.bailu.aat_gtk.ui.view.menu.model

class RadioItem(val group: Group, label: String, override var selected: Boolean,
                onSelect: (Boolean) -> Unit
) : SelectableItem(label, onSelect)