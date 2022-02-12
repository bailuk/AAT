package ch.bailu.aat_gtk.ui.view.map.control

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box

class MapBars  {
    companion object {
        const val TOP = 0
        const val LEFT = 1
        const val BOTTOM = 2
        const val RIGHT = 3
    }

    private val controlBars = HashMap<Int, Box>()

    fun add(bar: Box, pos: Int): Box {
        bar.visible = GTK.FALSE
        controlBars[pos] = bar

        when (pos) {
            TOP -> {
                bar.halign = Align.FILL
                bar.valign = Align.START
            } BOTTOM -> {
                bar.halign = Align.FILL
                bar.valign = Align.END
            } LEFT -> {
                bar.valign = Align.FILL
                bar.halign = Align.START
            } RIGHT -> {
                bar.valign = Align.FILL
                bar.halign = Align.END
            }

        }
        return bar
    }

    fun hide() {
        controlBars.forEach { it.value.visible = GTK.FALSE }
    }

    fun show(p: Int) {
        controlBars.forEach {
            if (it.key == p) {
                it.value.visible = GTK.TRUE
            } else {
                it.value.visible = GTK.FALSE
            }
        }
    }
}
