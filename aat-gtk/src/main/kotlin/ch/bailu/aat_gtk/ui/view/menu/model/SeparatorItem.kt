package ch.bailu.aat_gtk.ui.view.menu.model



class SeparatorItem() : Item(Type.SEPARATOR, { } ) {

    override val label: String
        get() = ""
    override val selected: Boolean
        get() = false
    override val group: Group
        get() = Group()

}