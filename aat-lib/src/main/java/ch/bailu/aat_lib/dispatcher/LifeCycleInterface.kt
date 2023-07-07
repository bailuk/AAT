package ch.bailu.aat_lib.dispatcher;

public interface LifeCycleInterface {

    void onResumeWithService();
    void onPauseWithService();
    void onDestroy();
}
