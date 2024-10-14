package ch.bailu.aat.preferences

import ch.bailu.aat_lib.preferences.SolidLong
import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.foc.Foc
import java.util.Objects

class SolidExportedDocument(storage: StorageInterface) : SolidString(storage, KEY){
    private val time = SolidExportedDocumentTime(storage)

    companion object {
        const val KEY = "SolidExportedDocument"
        const val LIMIT_MILLIS = 30 * 1000 // 1/2 minute
    }

    override fun setValue(v: String) {
        time.setValue(System.currentTimeMillis())
        super.setValue(v)
    }

    fun setDocument(file: Foc) {
        setValue(file.path);
    }

    fun isExportAllowed(path: String): Boolean {
        if (time.getValue() > System.currentTimeMillis() - LIMIT_MILLIS) {
            return Objects.equals(getValueAsString(), path)
        }
        return false
    }
}

private class SolidExportedDocumentTime(storage: StorageInterface): SolidLong(storage, KEY) {
    companion object {
        const val KEY = "SolidExportedDocumentTime"
    }
}
