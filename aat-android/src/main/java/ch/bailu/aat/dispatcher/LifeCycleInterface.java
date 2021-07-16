package ch.bailu.aat.dispatcher;

public interface LifeCycleInterface {

    void onResumeWithService();
    void onPauseWithService();
    void onDestroy();
}
