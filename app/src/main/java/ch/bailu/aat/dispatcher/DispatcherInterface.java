package ch.bailu.aat.dispatcher;

public interface DispatcherInterface {
    void addTarget(OnContentUpdatedInterface t, int... iid);
    void addSource(ContentSource s);


}
