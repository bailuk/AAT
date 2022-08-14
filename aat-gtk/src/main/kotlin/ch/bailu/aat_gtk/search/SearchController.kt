package ch.bailu.aat_gtk.search

import org.mapsforge.core.model.LatLong

class SearchController(private val searchModel: SearchModel) {
    var centerMap: (latLong: LatLong) -> Unit = {}
    var updateSpinner: () -> Unit = {}
    private val rest = RestNominatim()

    fun search(search: String) {
        if (search.isNotEmpty()) {
            rest.search(search) {
                if (it.ok) {
                    searchModel.updateSearchResult(it.json)
                    searchModel.withFirst { _, latLong ->
                        centerMap(latLong)
                    }
                }
                updateSpinner()
            }
            updateSpinner()
        }
    }
}
