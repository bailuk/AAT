package ch.bailu.aat_gtk.view.util

import ch.bailu.aat_gtk.util.EmptyStr
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.gtk.Label


fun Widget.margin(margin: Int) {
    this.marginEnd    = margin
    this.marginStart  = margin
    this.marginTop    = margin
    this.marginBottom = margin
}


class GtkLabel : Label(EmptyStr) {
    private var str: Str = EmptyStr
    var text = ""
    set(value) {
        val newStr = Str(value)
        label = newStr
        str.destroy()
        str = newStr
        field = value
    }

    fun setLeft(s: String) {
        text = s
        xalign = 0f
    }
}


fun String.truncate(max: Int = 30): String {
    if (this.length > max -1) {
        return "…" + this.substring(this.length - max, this.length)
    }
    return this
}