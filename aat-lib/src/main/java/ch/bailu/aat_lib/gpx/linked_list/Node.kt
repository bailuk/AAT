package ch.bailu.aat_lib.gpx.linked_list

open class Node {
    open var previous: Node? = null
    open var next: Node? = null

    companion object {
        const val SIZE_IN_BYTES: Long = 16
    }
}
