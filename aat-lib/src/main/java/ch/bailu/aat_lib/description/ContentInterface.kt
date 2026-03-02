package ch.bailu.aat_lib.description

/** Read-only interface to a label and formatted value string. */
interface ContentInterface {
    fun getLabel(): String
    fun getValueAsString(): String
}
