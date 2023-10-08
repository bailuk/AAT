package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory
import javax.annotation.Nonnull

open class SolidDataDirectory(
    private val defaultDirectory: SolidDataDirectoryDefault,
    focFactory: FocFactory?
) : SolidFile(
    defaultDirectory.getStorage(), SolidDataDirectory::class.java.simpleName, focFactory!!
) {
    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_directory_data()
    }

    @Nonnull
    override fun getValueAsString(): String {
        val r = super.getValueAsString()
        return if (getStorage().isDefaultString(r)) defaultDirectory.getValueAsString() else r
    }

    override fun hasKey(key: String): Boolean {
        return super.hasKey(key) || defaultDirectory.hasKey(key)
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        return defaultDirectory.buildSelection(list)
    }
}
