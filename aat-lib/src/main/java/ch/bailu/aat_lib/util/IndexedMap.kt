package ch.bailu.aat_lib.util

class IndexedMap<K, V> {
    private val keys = ArrayList<K>()
    private val map  = HashMap<K, V>()

    fun put(key: K, value: V) {
        if (!map.containsKey(key)) {
            keys.add(key)
        }
        map[key] = value
    }

    fun getValue(key: K) : V? {
        return map[key]
    }

    fun getValueAt(index: Int) : V? {
        return if (index > -1 && index < keys.size) {
            map[keys[index]]
        } else {
            null
        }
    }

    fun getKeyAt(index: Int) : K? {
        return if (index > -1 && index < keys.size) {
            keys[index]
        } else {
            null
        }
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
            val value = getValue(it)

            if (value != null) {
                function(it, value)
            }
        }
    }

    fun indexOfKey(key: K): Int {
        keys.forEachIndexed { index, it ->
            if (it == key) {
                return index
            }
        }
        return -1
    }
}
