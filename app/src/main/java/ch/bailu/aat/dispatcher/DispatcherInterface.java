package ch.bailu.aat.dispatcher;

public interface DispatcherInterface {
    void addTargets(OnContentUpdatedInterface t, int... iid);
    void addSource(ContentSource s);


}
