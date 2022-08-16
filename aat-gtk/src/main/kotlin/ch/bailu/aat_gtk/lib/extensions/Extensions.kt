package ch.bailu.aat_gtk.lib.extensions

import ch.bailu.aat_gtk.lib.icons.IconMap
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.ComboBoxText
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

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

fun Widget.margin(margin: Int) {
    this.marginEnd    = margin
    this.marginStart  = margin
    this.marginTop    = margin
    this.marginBottom = margin
}

fun String.ellipsize(max: Int): String {
    if (this.length <= max) return this
    return this.substring(0, max) + "…"
}

fun String.ellipsizeStart(max: Int): String {
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

fun ComboBoxText.appendText(text: String) {
    val str = Str(text)
    appendText(str)
    str.destroy()
}
