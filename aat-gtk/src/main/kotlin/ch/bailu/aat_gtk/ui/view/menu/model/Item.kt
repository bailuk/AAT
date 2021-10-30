package ch.bailu.aat_gtk.ui.view.menu.model


abstract class Item (val type: Type, val onSelected:(Item)-> Unit){
    abstract val label : String
    abstract val selected : Boolean
    abstract val group : Group
}