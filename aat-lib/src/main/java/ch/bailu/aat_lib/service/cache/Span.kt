package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.util.Rect


class Span {
    private var deg: Int
    private var firstPixel: Int
    private var lastPixel: Int

    constructor() {
        deg = -1
        firstPixel = 0
        lastPixel = -1
    }


    constructor(s: Span) {
        deg = s.deg
        firstPixel = s.firstPixel
        lastPixel = s.lastPixel
    }


    fun firstPixelIndex(): Int {
        return firstPixel
    }

    fun lastPixelIndex(): Int {
        return lastPixel
    }

    fun deg(): Int {
        return deg
    }


    fun pixelDim(): Int {
        if (deg > -1 && lastPixel >= firstPixel) {
            return lastPixel - firstPixel + 1
        }
        return 0
    }


    fun incrementAndCopyIntoArray(spanArray: ArrayList<Span>, pixelIndex: Int, deg: Int) {
        lastPixel = pixelIndex

        if (deg != this.deg) {
            copyIntoArray(spanArray)

            this.deg = deg
            firstPixel = pixelIndex
        }
    }

    fun copyIntoArray(l: ArrayList<Span>) {
        if (pixelDim() > 0) l.add(Span(this))
    }

    companion object {
        @JvmStatic
        fun toRect(laSpan: Span, loSpan: Span): Rect {
            val r = Rect()
            r.top = laSpan.firstPixel
            r.bottom = laSpan.lastPixel
            r.left = loSpan.firstPixel
            r.right = loSpan.lastPixel
            return r
        }
    }
}
