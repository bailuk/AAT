package ch.bailu.aat_gtk.solid

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_lib.preferences.StorageInterface

object SolidWindowSize {
    private const val windowWidth = "windowWidth"
    private const val windowHeight = "windowHeight"

    fun writeSize(storageInterface: StorageInterface, width: Int, height: Int) {
        storageInterface.writeInteger(windowWidth, width)
        storageInterface.writeInteger(windowHeight, height)
    }

    fun readWidth(storageInterface: StorageInterface): Int {
        return valueOrDefault(storageInterface.readInteger(windowWidth), Layout.windowWidth)
    }

    fun readHeight(storageInterface: StorageInterface): Int {
        return valueOrDefault(storageInterface.readInteger(windowHeight), Layout.windowHeight)
    }

    private fun valueOrDefault(value: Int, defaultValue: Int): Int {
        return if (value >= Layout.windowMinSize) {
            value
        } else {
            defaultValue
        }
    }
}
