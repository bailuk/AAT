package ch.bailu.aat.services.tileremover;

public interface State {

    void scan();
    void stop();
    void reset();
    void remove();
    void rescan();

}
