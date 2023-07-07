package ch.bailu.aat.util

import android.content.Intent
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.util.Objects
import org.mapsforge.core.model.BoundingBox

object AppIntent {
    private const val EXTRA_FILE = "file"
    private const val EXTRA_URL = "source"
    const val EXTRA_MESSAGE = "source"
    private val KEYS = arrayOf("file", "source", "c", "d", "e", "f")

    fun setUrl(intent: Intent, url: String?) {
        intent.putExtra(EXTRA_URL, url)
    }

    @JvmStatic
    fun getUrl(intent: Intent): String? {
        return intent.getStringExtra(EXTRA_URL)
    }

    fun setFile(intent: Intent, file: String?) {
        intent.putExtra(EXTRA_FILE, file)
    }

    @JvmStatic
    fun getFile(intent: Intent): String? {
        return intent.getStringExtra(EXTRA_FILE)
    }

    @JvmStatic
    fun hasFile(intent: Intent, file: String?): Boolean {
        return Objects.equals(intent.getStringExtra(EXTRA_FILE), file)
    }

    fun setBoundingBox(intent: Intent, box: BoundingBox) {
        intent.putExtra("N", (box.maxLatitude * 1E6).toInt())
        intent.putExtra("E", (box.maxLongitude * 1E6).toInt())
        intent.putExtra("S", (box.minLatitude * 1E6).toInt())
        intent.putExtra("W", (box.minLongitude * 1E6).toInt())
    }

    fun getBoundingBox(intent: Intent): BoundingBoxE6 {
        return BoundingBoxE6(
            intent.getIntExtra("N", 0),
            intent.getIntExtra("E", 0),
            intent.getIntExtra("S", 0),
            intent.getIntExtra("W", 0)
        )
    }

    fun toIntent(action: String, vararg args: String): Intent {
        val size = Math.min(args.size, KEYS.size)
        val result = Intent(action)
        result.putExtra("size", size)
        for (i in 0 until size) {
            result.putExtra(KEYS[i], args[i])
        }
        return result
    }

    fun toArgs(intent: Intent): Array<out String> {
        val size = Math.min(intent.getIntExtra("size", 0), KEYS.size)
        val result = ArrayList<String>()
        for (i in 0 until size) {
            result.add(toSaveString(intent.getStringExtra(KEYS[i])))
        }
        return result.toTypedArray()
    }

    private fun toSaveString(stringExtra: String?): String {
        if (stringExtra == null) {
            return ""
        }
        return stringExtra
    }
}
