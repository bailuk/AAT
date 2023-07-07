package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface

abstract class ContentDescription : ContentInterface, OnContentUpdatedInterface {
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
