package ch.bailu.aat_lib.gpx.linked_list

class List {
    var first: Node? = null
        private set

    var last: Node? = null
        private set

    private var count = 0

    fun append(node: Node) {
        node.previous = last
        if (first == null) {
            first = node
        } else {
            last!!.next = node
        }
        last = node
        count++
    }

    fun size(): Int {
        return count
    }
}
