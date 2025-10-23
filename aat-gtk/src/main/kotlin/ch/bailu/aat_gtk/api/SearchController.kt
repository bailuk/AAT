package ch.bailu.aat_gtk.api

import ch.bailu.aat_lib.file.json.SearchModel
import org.mapsforge.core.model.LatLong

class SearchController(private val searchModel: SearchModel) {
    var centerMap: (latLong: LatLong) -> Unit = {}
    var updateSpinner: () -> Unit = {}
    private val rest = RestNominatim()

    init {
        searchModel.updateSearchResult(rest.restClient.json)
    }

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
