package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import org.mapsforge.core.util.Parameters


class SolidRendererThreads(storageInterface: StorageInterface) : SolidIndexList(storageInterface, KEY) {
    override fun length(): Int {
        return values.size
    }

    override fun getValueAsString(index: Int): String {
        return toDefaultString(values[index].toString(), index)
    }

    override fun getLabel(): String {
        return Res.str().p_render_threads()
    }

    val value: Int
        get() = values[index]

    companion object {
        private const val KEY = "renderer_threads"
        private val values = intArrayOf(numberOfBackgroundThreats(), 2, 3, 4, 1)
        fun numberOfBackgroundThreats(): Int {
            var result = numberOfCores() - 1
            result = result.coerceAtMost(3)
            result = result.coerceAtLeast(1)
            return result
        }

        private fun numberOfCores(): Int {
            return try {
                Runtime.getRuntime().availableProcessors().coerceAtLeast(1)
            } catch (e: Exception) {
                1
            }
        }

        fun set() {
            Parameters.NUMBER_OF_THREADS = numberOfBackgroundThreats()
        }
    }
}
