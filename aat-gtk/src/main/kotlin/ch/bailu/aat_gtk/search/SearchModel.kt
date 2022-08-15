package ch.bailu.aat_gtk.search

import ch.bailu.aat_gtk.lib.json.JsonMap
import org.mapsforge.core.model.LatLong

class SearchModel {
    private data class Result(val name: String, val latLong: LatLong)

    private val result = ArrayList<Result>()
    private val observers = ArrayList<(SearchModel)->Unit>()

    fun observe(observer: (SearchModel) -> Unit) {
        observers.add(observer)
        observer(this)
    }

    fun updateSearchResult(json: JsonMap) {
        parse(json)
        observers.forEach { it(this) }
    }

    private fun parse(json: JsonMap) {
        result.clear()
        json.map("result") { it ->
            var lat = 0.0
            var lon = 0.0
            var name = ""
            it.string("lat") {
                lat = it.toDouble()
            }
            it.string("lon") {
                lon = it.toDouble()
            }

            it.string("display_name") {
                name = it
            }
            result.add(Result(name, LatLong(lat, lon)))
        }
    }

    fun withFirst(cb : (String, LatLong) -> Unit) {
        val first = result.firstOrNull()
        if (first is Result) {
            cb(first.name, first.latLong)
        }
    }

    fun forEach(cb: (String, LatLong) -> Unit) {
        result.forEach {
            cb(it.name, it.latLong)
        }
    }
}
