package ch.bailu.aat_gtk.lib.extensions

import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.type.Str

fun String.ellipsize(max: Int): String {
    if (this.length <= max) return this
    return this.substring(0, max) + "…"
}

fun Label.setMarkup(string: String) {
    val str = Str(string)
    setMarkup(str)
    str.destroy()
}

fun Label.setLabel(string: String) {
    val str = Str(string)
    label = str
    str.destroy()
}

fun Label.setText(string: String) {
    val str = Str(string)
    text = str
    str.destroy()
}

fun Button.setLabel(string: String) {
    val str = Str(string)
    label = str
    str.destroy()
}
