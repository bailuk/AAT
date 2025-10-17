package ch.bailu.aat.providers

import android.content.Context
import ch.bailu.aat.preferences.SolidExportedDocument
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.presets.SolidMET
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

class ProviderContext(private val context: Context) {

    fun getTrackListDirectory(preset: Int): Foc {
        val solidDataDirectory = AndroidSolidDataDirectory(context)
        return AppDirectory.getTrackListDirectory(solidDataDirectory, preset)
    }

    fun getPresetCount(): Int {
        return SolidPresetCount(Storage(context)).value
    }

     fun getPresetName(preset: Int): String {
        return SolidMET(Storage(context), preset).getValueAsString()
    }

    fun isExportAllowed(path: String): Boolean {
        return SolidExportedDocument(Storage(context)).isExportAllowed(path)

    }
}
