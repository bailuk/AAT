package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.dispatcher.TargetInterface

/**
 * Base class for a displayable label+value+unit triple that updates from [GpxInformation].
 *
 * Implements [TargetInterface] so it can be wired into the [Dispatcher] chain
 * (typically wrapped by a [NumberView]). Subclasses override [onContentUpdated]
 * to extract sensor-specific fields from [GpxInformation.getAttributes].
 */
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
