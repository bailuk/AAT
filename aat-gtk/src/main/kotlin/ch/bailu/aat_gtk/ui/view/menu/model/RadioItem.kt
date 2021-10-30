package ch.bailu.aat_gtk.ui.view.menu.model

class RadioItem(val group: Group, label: String, var selected: Boolean,
                onSelect: (Item) -> Unit) : LabelItem(label, onSelect)
{
    val index = group.next()
}