package ch.bailu.aat.services.tileremover;

public interface State {

    void scan();
    void stop();
    void reset();
    void resetAndRescan();
    void remove();
    void rescan();

}
