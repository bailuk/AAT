package ch.bailu.aat_lib.dispatcher

/**
 * Connect sources with targets
 */
interface DispatcherInterface {
    /**
     * Add a target to this dispatcher. This target will then receive messages from this
     * dispatchers sources. Messages will be filtered by the provided info ids.
     * @param target: receiver of a message
     * @param iid list of info ids: only transmits messages having one of these ids
     */
    fun addTarget(target: OnContentUpdatedInterface, vararg iid: Int)

    /**
     * Add a source to this dispatcher. This dispatcher will dispatch all messages that pass
     * the an info id filter from this source to its connected targets.
     * @param source that transmits messages
     */
    fun addSource(source: ContentSourceInterface)
    fun requestUpdate()
}
