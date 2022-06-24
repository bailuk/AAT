package ch.bailu.aat_gtk.view.stack

import ch.bailu.gtk.gtk.ComboBoxText
import ch.bailu.gtk.type.Str

class ComboBoxString {
    val combo = ComboBoxText()

    private val map = HashMap<Int, Str>()

    fun insert(position: Int, text: String) {
        val toDestroy = map[position]
        val strText = Str(text)
        map[position] = strText
        combo.remove(position)
        combo.insertText(position, strText)

        if (toDestroy is Str) {
            toDestroy.destroy()
        }
    }
}
