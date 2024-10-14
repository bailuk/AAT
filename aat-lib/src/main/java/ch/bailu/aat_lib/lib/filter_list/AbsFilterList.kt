package ch.bailu.aat_lib.lib.filter_list

abstract class AbsFilterList<T> {
    private val visible = ArrayList<T>(100)
    private val all = ArrayList<T>(100)

    private var filterKeys = KeyList()

    fun filter(s: String) {
        filterKeys = KeyList(s)
        filterAll()
    }

    fun filterAll() {
        visible.clear()

        for (t in all) {
            if (showElement(t, filterKeys)) visible.add(t)
        }
    }

    abstract fun showElement(t: T, keyList: KeyList): Boolean

    fun add(t: T) {
        all.add(t)

        if (showElement(t, filterKeys)) visible.add(t)
    }

    fun getFromAll(index: Int): T {
        return all[index]
    }

    fun getFromVisible(index: Int): T {
        return visible[index]
    }

    fun clear() {
        visible.clear()
        all.clear()
    }

    fun sizeVisible(): Int {
        return visible.size
    }

    fun sizeAll(): Int {
        return all.size
    }
}
