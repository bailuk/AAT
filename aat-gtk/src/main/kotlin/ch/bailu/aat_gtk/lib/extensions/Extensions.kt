package ch.bailu.aat_gtk.lib.extensions

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.icons.IconMap
import ch.bailu.gtk.gtk.*

fun Widget.margin(margin: Int) {
    this.marginEnd    = margin
    this.marginStart  = margin
    this.marginTop    = margin
    this.marginBottom = margin
}

fun String.ellipsize(max: Int = 30): String {
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

fun Button.setIcon(imageResource: String, size: Int = Layout.ICON_SIZE) {
    val image = IconMap.getImage(imageResource, size)
    image.margin(Layout.margin)
    child = image
}
