package ch.bailu.aat_gtk.ui.view.menu.model

data class Group(val id: Int = counter++) {
    private var size = 0

    companion object {
        var counter = 0
    }

    fun next(): Int {
        size++
        return size
    }
}
