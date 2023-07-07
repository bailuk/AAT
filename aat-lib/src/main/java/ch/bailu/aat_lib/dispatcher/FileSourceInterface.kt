package ch.bailu.aat_lib.dispatcher

import ch.bailu.foc.Foc

interface FileSourceInterface : ContentSourceInterface {
    fun setFile(file: Foc)
    fun setEnabled(enabled: Boolean)
    fun isEnabled(): Boolean
}
