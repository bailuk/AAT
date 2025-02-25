package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.information.InfoID

/**
 * Connect sources with targets
 */
interface DispatcherInterface: EmitterInterface, TargetInterface {
    /**
     * Add a source to this dispatcher. This dispatcher will dispatch all messages that pass
     * the an info id filter from this source to its connected targets.
     * @param source that transmits messages
     */
    fun addSource(source: SourceInterface)
    /**
     * Add a target to this chain. This target will then receive messages from this
     * dispatchers sources. Messages will be filtered by the provided info ids.
     * @param target: receiver of a message
     * @param iid list of info ids: only transmits messages having one of these ids
     */
    fun addTarget(target: TargetInterface, vararg iid: Int = intArrayOf(InfoID.ALL))
}
