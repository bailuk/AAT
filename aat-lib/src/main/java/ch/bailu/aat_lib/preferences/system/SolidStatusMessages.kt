package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res


class SolidStatusMessages(storage: StorageInterface) : SolidIndexList(storage, KEY) {
    override fun length(): Int {
        return VAL.size
    }

    override fun getValueAsString(index: Int): String {
        return VAL[index]
    }

    
    override fun getLabel(): String {
        return Res.str().p_messages()
    }

    fun showPath(): Boolean {
        return index == 2
    }

    fun showURL(): Boolean {
        return index == 1 || index == 2
    }

    fun showSummary(): Boolean {
        return index == 0
    }

    companion object {
        private const val KEY = "Status Messages"
        private val VAL = arrayOf(
            Res.str().p_messages_size(),
            Res.str().p_messages_url(),
            Res.str().p_messages_file(),
            Res.str().none()
        )
    }
}
