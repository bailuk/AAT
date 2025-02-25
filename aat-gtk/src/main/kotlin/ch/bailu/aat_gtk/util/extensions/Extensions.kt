package ch.bailu.aat_gtk.util.extensions

import ch.bailu.gtk.gtk.Widget

fun Widget.margin(margin: Int) {
    this.marginEnd    = margin
    this.marginStart  = margin
    this.marginTop    = margin
    this.marginBottom = margin
}
