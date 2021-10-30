package ch.bailu.aat_gtk.ui.view.menu.model

data class Group(val id: Long = System.currentTimeMillis()) {
    private var size = 0

    fun next(): Int {
        size++
        return size
    }
}
