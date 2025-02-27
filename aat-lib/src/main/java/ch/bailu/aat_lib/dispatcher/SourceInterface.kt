package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.information.GpxInformationProvider

interface SourceInterface : GpxInformationProvider, EmitterInterface {
    fun setTarget(target: TargetInterface)
    fun getIID(): Int
}
