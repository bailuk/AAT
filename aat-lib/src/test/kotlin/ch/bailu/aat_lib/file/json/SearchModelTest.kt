package ch.bailu.aat_lib.file.json

import ch.bailu.aat_lib.lib.json.parser.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mapsforge.core.model.LatLong

class SearchModelTest {

    val jsonContent = "{\n" +
            "  \"result\": [\n" +
            "    {\n" +
            "      \"place_id\": 84487585,\n" +
            "      \"licence\": \"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\n" +
            "      \"osm_type\": \"relation\",\n" +
            "      \"osm_id\": 1683619,\n" +
            "      \"lat\": \"47.5581077\",\n" +
            "      \"lon\": \"7.5878261\",\n" +
            "      \"class\": \"boundary\",\n" +
            "      \"type\": \"administrative\",\n" +
            "      \"place_rank\": 16,\n" +
            "      \"importance\": 0.7089166516837468,\n" +
            "      \"addresstype\": \"city\",\n" +
            "      \"name\": \"Basel\",\n" +
            "      \"display_name\": \"Basel, Basel-Stadt, Schweiz/Suisse/Svizzera/Svizra\",\n" +
            "      \"boundingbox\": [\n" +
            "        \"47.5192940\",\n" +
            "        \"47.5898969\",\n" +
            "        \"7.5546608\",\n" +
            "        \"7.6341406\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n"
    @Test
    fun testSearchModel() {
        var index = 0
        val jsonMap = Json.parse(jsonContent)
        val searchModel = SearchModel()
        searchModel.updateSearchResult(jsonMap)
        searchModel.forEachIndexed { i: Int,  name: String, latLong: LatLong ->
            assertEquals("Basel, Basel-Stadt, Schweiz/Suisse/Svizzera/Svizra", name)
            assertEquals(47.5581077, latLong.latitude)
            assertEquals(7.5878261, latLong.longitude)
            assertEquals(index, i)
            index++
        }
        assertEquals(1, index)
    }
}
