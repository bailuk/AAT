package ch.bailu.aat_gtk.ui.view.menu.model

class LabelItem(override val label: String, onSelected: (Item) -> Unit) : Item(Type.LABEL,
    onSelected
) {

    override val selected: Boolean
        get() = true
    override val group: Group
        get() = Group()
}