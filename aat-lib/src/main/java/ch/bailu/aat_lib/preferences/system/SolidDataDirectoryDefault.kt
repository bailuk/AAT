package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.foc.FocFactory


abstract class SolidDataDirectoryDefault(storage: StorageInterface, focFactory: FocFactory) : SolidFile(
    storage, SolidDataDirectoryDefault::class.java.simpleName, focFactory
) {
    
    override fun getValueAsString(): String {
        val r = super.getValueAsString()
        return if (getStorage().isDefaultString(r)) {
            setDefaultValue()
        } else r
    }

    open fun setDefaultValue(): String {
        val r = defaultValue
        setValue(r)
        return r
    }

    private val defaultValue: String
        get() {
            var list = ArrayList<String>(5)
            list = buildSelection(list)
            list.add(getStorage().getDefaultString()) // failsave
            return list[0]
        }
}
