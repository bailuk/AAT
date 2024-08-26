package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.dispatcher.TargetInterface

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
