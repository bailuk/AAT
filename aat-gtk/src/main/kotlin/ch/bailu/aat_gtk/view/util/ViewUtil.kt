package ch.bailu.aat_gtk.view.util

import ch.bailu.aat_gtk.util.EmptyStr
import ch.bailu.aat_gtk.util.IconMap
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str


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
}


fun String.truncate(max: Int = 30): String {
    if (this.length > max -1) {
        return "…" + this.substring(this.length - max, this.length)
    }
    return this
}

fun String.escapeUnderscore(): String {
    return replace("_", "__")
}

fun Button.setIcon(imageResource: String, size: Int = Bar.ICON_SIZE) {
    val image = IconMap.getImage(imageResource, size)
    image.margin(Bar.MARGIN)
    child = image
}

fun Label.setText(text: String) {
    label = Str(text)
}

fun CheckButton.setText(text: String) {
    label = Str(text)
}
