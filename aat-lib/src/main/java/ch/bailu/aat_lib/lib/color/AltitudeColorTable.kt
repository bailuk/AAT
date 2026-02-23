package ch.bailu.aat_lib.lib.color

import ch.bailu.aat_lib.app.AppColor
import ch.bailu.aat_lib.util.Limit.clamp

object AltitudeColorTable {
    private val colorTable: IntArray = IntArray(SIZE)

    init {
        for (i in 0..<SIZE) {
            colorTable[i] = calculateColor(i)
        }
    }

    private fun calculateColor(index: Int): Int {
        val gradient: Int = index / GRADIENT_SIZE
        val colorIndex: Int = index % GRADIENT_SIZE

        return calculateColor(gradient, colorIndex)
    }

    private fun calculateColor(gradient: Int, i: Int): Int {
        return when (gradient) {
            0 -> rgb(0, i, i)
            1, 7 -> rgb(0, MAX - i, MAX)
            2, 8 -> rgb(i, 0, MAX)
            3, 9 -> rgb(MAX, 0, MAX - i)
            4 -> rgb(MAX, i, 0)
            5 -> rgb(MAX - i, MAX, 0)
            6 -> rgb(0, MAX, i)
            10 -> rgb(MAX - i, 0, 0)
            11 -> rgb(i, i, i)
            else -> AppColor.HL_ORANGE
        }
    }

    private fun rgb(r: Int, g: Int, b: Int): Int {
        return ARGB(r, g, b).toInt()
    }


    fun getColor(index: Int): Int {
        return getColorAtIndex(index + ALTITUDE_OFFSET)
    }

    private fun getColorAtIndex(index: Int): Int {
        var index = index
        index = clamp(index, 0, colorTable.size - 1)
        return colorTable[index]
    }

    private const val ALTITUDE_OFFSET = 500
    private const val GRADIENTS = 12
    private const val GRADIENT_SIZE = 256
    private const val SIZE: Int = GRADIENTS * GRADIENT_SIZE
    private const val MAX: Int = GRADIENT_SIZE - 1
}
