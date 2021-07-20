package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;

/**
 * Connect sources with targets
 */
public interface DispatcherInterface {

    /**
     * Add a target to this dispatcher. This target will then receive messages from this
     * dispatchers sources. Messages will be filtered by the provided info ids.
     * @param target: receiver of a message
     * @param iid list of info ids: only transmits messages having one of these ids
     */
    void addTarget(OnContentUpdatedInterface target, int... iid);

    /**
     * Add a source to this dispatcher. This dispatcher will dispatch all messages that pass
     * the an info id filter from this source to its connected targets.
     * @param source that transmits messages
     */
    void addSource(ContentSource source);


}
