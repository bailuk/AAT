package ch.bailu.aat_lib.util

class IndexedMap<K, V> {
    private val keys = ArrayList<K>()
    private val map  = HashMap<K, V>()

    fun put(key: K, value: V) {
        keys.add(key)
        map[key] = value
    }

    fun get(key: K) : V? {
        return map[key]
    }

    fun getAt(index: Int) : V? {
        return map[keys[index]]
    }

    fun size(): Int {
        return keys.size
    }

    fun remove(key: K)  {
        map.remove(key)
        keys.remove(key)
    }

    fun forEach(function: (K, V) -> Unit) {
        keys.forEach {
            val value = get(it)

            if (value != null) {
                function(it, value)
            }
        }
    }

    fun indexOf(key: K): Int {
        keys.forEachIndexed { index, it ->
            if (it == key) {
                return index
            }
        }
        return -1
    }
}