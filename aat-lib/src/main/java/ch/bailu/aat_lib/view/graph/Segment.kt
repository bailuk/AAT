package ch.bailu.aat_lib.view.graph


class Segment {
    private var start = -1
    private var end = -1

    fun setLimit(start: Int, end: Int) {
        this.start = start
        this.end = end
    }

    fun isValid(): Boolean {
        return start > -1 && end > start
    }

    fun isAfter(index: Int): Boolean {
        return index > end
    }

    fun isBefore(index: Int): Boolean {
        return index < start
    }

    fun isInside(index: Int): Boolean {
        return index >= start && index <= end
    }
}
