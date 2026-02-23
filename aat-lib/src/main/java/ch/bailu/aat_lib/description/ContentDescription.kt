package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.dispatcher.TargetInterface

/** Base class for a displayable label+value+unit triple that updates from [GpxInformation]. */
abstract class ContentDescription : ContentInterface, TargetInterface {
    abstract fun getValue(): String

    open fun getLabelShort(): String {
        return getLabel()
    }

    override fun getValueAsString(): String {
        return "${getValue()} ${getUnit()}"
    }

    open fun getUnit(): String {
        return ""
    }

    companion object {
        const val VALUE_DISABLED = "--"
    }
}
