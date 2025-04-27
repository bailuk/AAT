package ch.bailu.aat_lib.lib.json.parser

import com.google.gson.Gson
import java.io.File

object Json {
    private val empty = JsonMap(HashMap<String, String>())

    fun parse(file : File) : JsonMap {
        return try {
            parse(file.readText())
        } catch (e :Exception) {
            empty
        }
    }

    fun parse(json: String) : JsonMap {
        return try {
            val parsed = Gson().fromJson(json, Map::class.java)
            JsonMap(parsed)
        } catch (e: Exception) {
            empty
        }
    }
}
