package ch.bailu.aat_lib.view.cockpit

import ch.bailu.aat_lib.description.ContentDescription
import kotlin.math.max

class Layouter(private val items: MutableList<ContentDescription>, doPlacement: DoPlacement) {
    private var parentWidth = 0
    private var parentHeight = 0

    private val placer: Placer = Placer({ index: Int, x: Int, y: Int, w: Int, h: Int -> })
    private val realPlacer: Placer = Placer(doPlacement)

    fun layout(w: Int, h: Int) {
        var chars = 1

        parentWidth = w
        parentHeight = h

        while (chars < MAX_CHARS_PER_LINE) {
            if (tryPlacement(chars)) {
                doPlacement(chars)
                break
            } else chars++
        }
    }

    private fun tryPlacement(charsPerLine: Int): Boolean {
        placer.place(charsPerLine)
        return placeItems(placer)
    }

    private fun doPlacement(charsPerLine: Int): Boolean {
        realPlacer.place(charsPerLine)
        return placeItems(realPlacer)
    }

    private fun placeItems(p: Placer): Boolean {
        var works = true

        val size = items.size
        var i = 0
        while (i < size && works) {
            works = p.placeItem(i)
            i++
        }
        return works
    }

    private inner class Placer(private val doPlacement: DoPlacement) {
        private var charWidth = 0
        private var charHeight = 0
        private var xPos: Int = 0
        private var yPos: Int = 0

        fun place(charsPerLine: Int) {
            this.yPos = 0
            this.xPos = 0
            calculateCharGeometry(charsPerLine)
        }

        fun calculateCharGeometry(charsPerLine: Int) {
            charWidth = parentWidth / charsPerLine

            charHeight = charWidth * RATIO
            if (charHeight > parentHeight) {
                charHeight = parentHeight
                charWidth = charHeight / RATIO
            }
        }

        fun placeItem(index: Int): Boolean {
            var works = true
            val width = getWidthOfView(index)

            if (width > parentWidth) {
                works = false
            } else if (width + this.xPos > parentWidth) {
                works = addLine()
                if (works) works = placeItem(index)
            } else {
                setGeometry(index, width, charHeight)
                this.xPos += width
            }
            return works
        }

        private fun setGeometry(index: Int, width: Int, height: Int) {
            doPlacement.setGeometry(index, this.xPos, this.yPos, this.xPos + width, this.yPos + height)
        }

        fun addLine(): Boolean {
            this.yPos += charHeight
            this.xPos = 0
            return ((charHeight + this.yPos) <= parentHeight)
        }

        fun getWidthOfView(index: Int): Int {
            var len = items[index].getValue().length
            len = max(len, MIN_CHARS)
            return len * charWidth
        }
    }

    fun interface DoPlacement {
        fun setGeometry(index: Int, x: Int, y: Int, x2: Int, y2: Int)
    }

    companion object {
        private const val MAX_CHARS_PER_LINE = 50
        private const val MIN_CHARS = 4
        private const val RATIO = 3

    }
}
