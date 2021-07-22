package ch.bailu.aat_lib.service.tracker;

public interface StateInterface {

    void updateTrack();

    void onStartPauseResume();
    void onStartStop();
    void onPauseResume();

    int getStateID();

    String getStartStopText();
    String getPauseResumeText();

    int getStartStopIconID();
}
