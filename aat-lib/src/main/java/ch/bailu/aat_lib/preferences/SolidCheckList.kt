package ch.bailu.aat_lib.preferences

abstract class SolidCheckList : AbsSolidType() {

    abstract fun getStringArray(): Array<String>
    abstract fun getEnabledArray(): BooleanArray

    abstract fun setEnabled(index: Int, isChecked: Boolean)
    override fun setValueFromString(string: String) {}

    override fun getValueAsString(): String {
        return ""
    }
}
