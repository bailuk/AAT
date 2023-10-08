package ch.bailu.aat_lib.preferences.presets

import ch.bailu.aat_lib.preferences.SolidStaticIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import javax.annotation.Nonnull

class SolidMissingTrigger(storage: StorageInterface, i: Int) : SolidStaticIndexList(
    storage, KEY + i, labels
) {
    val triggerSeconds: Int
        get() = VALUE_LIST[index]
    val triggerMillis: Int
        get() = triggerSeconds * 1000

    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_missing_trigger()
    }

    override fun getToolTip(): String? {
        return Res.str().tt_p_missing_trigger()
    }

    companion object {
        private const val KEY = "missing_trigger_"
        private val VALUE_LIST = intArrayOf(
            999,
            10,
            15,
            20,
            25,
            30,
            40,
            50,
            100,
            200
        )
        private var labels: Array<String> = generateLabelList()


        private fun generateLabelList(): Array<String> {
            val labels = ArrayList<String>(VALUE_LIST.size)
            labels.add(Res.str().off())
            for (i in 1 until VALUE_LIST.size) {
                labels.add(VALUE_LIST[i].toString() + "s")
            }
            return labels.toTypedArray()
        }
    }
}
