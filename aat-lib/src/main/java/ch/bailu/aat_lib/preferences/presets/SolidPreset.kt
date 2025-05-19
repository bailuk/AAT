package ch.bailu.aat_lib.preferences.presets

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc


class SolidPreset(storage: StorageInterface) : SolidIndexList(storage, KEY) {
    override fun length(): Int {
        return SolidPresetCount(getStorage()).value
    }

    public override fun getValueAsString(index: Int): String {
        return smet(index).getValueAsString()
    }


    override fun getValueAsString(): String {
        return smet().getValueAsString()
    }

    private fun smet(): SolidMET {
        return smet(index)
    }

    private fun smet(i: Int): SolidMET {
        return SolidMET(getStorage(), i)
    }

    override fun hasKey(key: String): Boolean {
        return super.hasKey(key) || smet().hasKey(key)
    }


    override fun getLabel(): String {
        return Res.str().p_preset()
    }

    fun getDirectory(sdirectory: SolidDataDirectory): Foc {
        return AppDirectory.getTrackListDirectory(sdirectory, index)
    }

    companion object {
        const val KEY = "preset"
        @JvmStatic
        fun getPresetFromFile(file: Foc): Int {
            var preset = 0
            val parent = file.parent()
            if (parent != null && parent.isDir) {
                var name = parent.name
                if (name != null) {
                    name = name.replace(AppDirectory.PRESET_PREFIX, "")
                    try {
                        preset = name.toInt()
                        preset = Math.max(preset, 0)
                        preset = Math.min(preset, SolidPresetCount.MAX)
                    } catch (e: Exception) {
                        preset = 0
                    }
                }
            }
            return preset
        }
    }
}
