package ch.bailu.aat.preferences.system

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.fs.AndroidVolumes
import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc_android.FocAndroidFactory

class AndroidSolidDataDirectoryDefault(val context: Context) : SolidDataDirectoryDefault(
    Storage(
        context
    ), FocAndroidFactory(context)
) {

    override fun getValueAsString(): String {
        val r = super.getValueAsString()
        return if (getStorage().isDefaultString(r)) setDefaultValue() else r
    }

    override fun setDefaultValue(): String {
        val r = defaultValue
        setValue(r)
        return r
    }

    // failsave
    private val defaultValue: String
        get() {
            var list = ArrayList<String>(5)
            list = buildSelection(list)
            list.add(getStorage().defaultString) // failsave
            return list[0]
        }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val volumes = AndroidVolumes(context)


        // volume/aat_data (exists and is writeable)
        for (vol in volumes.volumes) {
            val aatData = vol.child(AppDirectory.DIR_AAT_DATA)
            SelectionList.add_w(list, aatData)
        }

        // volume/aat_data (does not exist but can be created)
        for (vol in volumes.volumes) {
            val aatData = vol.child(AppDirectory.DIR_AAT_DATA)
            if (!aatData.exists()) SelectionList.add_w(list, vol, aatData)
        }

        // app_private/files (writeable and on external medium)
        val files = volumes.files
        for (i in 1 until files.size) {
            SelectionList.add_w(list, files[i])
        }

        // app_private/files (read only and on external medium)
        for (i in 1 until files.size) {
            SelectionList.add_ro(list, files[i])
        }

        // volume/aat_data (read only)
        for (vol in volumes.volumes) {
            val aatData = vol.child(AppDirectory.DIR_AAT_DATA)
            SelectionList.add_ro(list, vol, aatData)
        }

        // app_private/files (readable and internal)
        if (files.isNotEmpty()) SelectionList.add_r(list, files[0])
        return list
    }
}
