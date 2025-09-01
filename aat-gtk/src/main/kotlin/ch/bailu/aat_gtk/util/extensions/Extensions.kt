package ch.bailu.aat_gtk.util.extensions

import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.foc.Foc
import ch.bailu.gtk.gtk.Widget

fun Widget.margin(margin: Int) {
    this.marginEnd    = margin
    this.marginStart  = margin
    this.marginTop    = margin
    this.marginBottom = margin
}

fun Foc.toPathString(): String {
    if (this.isFile) {
        val parent = this.parent()
        if (parent is Foc) {
            return this.path
        }
    } else if (this.isDir) {
        return this.path
    }
    return ""
}

fun Widget.setTooltipText(solid: AbsSolidType) {
    val tooltip = solid.getToolTip()
    if (tooltip is String) {
        this.setTooltipText(tooltip)
    }
}
