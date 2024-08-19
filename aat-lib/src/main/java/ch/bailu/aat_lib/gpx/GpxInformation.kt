package ch.bailu.aat_lib.gpx

import ch.bailu.foc.Foc

open class GpxInformation : GpxDataWrapper() {
    open fun getLoaded(): Boolean {
        return false
    }

    open fun getGpxList(): GpxList {
        return GpxList.NULL_TRACK
    }

    open fun getAccuracy(): Float {
        return 0f
    }

    open fun getState(): Int {
        return StateID.ON
    }

    open fun getFile(): Foc {
        return Foc.FOC_NULL
    }

    companion object {
        @JvmField
        val NULL: GpxInformation = GpxInformation()
    }
}
