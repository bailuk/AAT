package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.preferences.OnPresetPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import java.io.Closeable
import javax.annotation.Nonnull

abstract class LocationStackItem : Closeable, OnPresetPreferencesChanged {
    abstract fun passState(state: Int)
    abstract fun passLocation(location: LocationInformation)

    override fun close() {}

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {}

    open fun appendStatusText(builder: StringBuilder) {
        builder.append("<b>")
        builder.append(javaClass.simpleName)
        builder.append("</b><br>")
    }
}
