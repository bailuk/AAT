package ch.bailu.aat_lib.lib.json.parser

class JsonMap(private val map: Map<*,*> ) {
    fun map(key: String, call: (JsonMap) -> Unit) {
        val o = map[key]
        if (o is Map<*,*>) {
            call(JsonMap(o))
        } else if (o is List<*>) {
            o.forEach {
                if (it is Map<*, *>) {
                    call(JsonMap(it))
                }
            }
        }
    }

    fun string(key: String, call: (key: String, value: String) -> Unit) {
        val o = map[key]
        if (o is String) {
            call(key, o)
        } else if (o is Map<*,*>) {
            o.iterator().forEach {
                val k = it.key
                val v = it.value
                if (k is String && v is String) {
                    call(k, v)
                }
            }
        }
    }

    fun string(key: String, call: (String) -> Unit) {
        val o = map[key]
        if (o is String) {
            call(o)
        } else if (o is List<*>) {
            o.forEach {
                if (it is String) {
                    call(it)
                }
            }
        }
    }

    fun number(key: String, call: (Double) -> Unit) {
        val o = map[key]
        if (o is Double) {
            call(o)
        } else if (o is List<*>) {
            o.forEach {
                if (it is Double) {
                    call(it)
                }
            }
        }
    }
}
