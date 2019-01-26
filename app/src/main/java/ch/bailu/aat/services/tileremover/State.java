package ch.bailu.aat.services.tileremover;

public interface State {

    /** firstPixelIndex scanning **/
    void scan();

    /** chancel **/
    void stop();

    /** setItem to initial state **/
    void reset();

    /** firstPixelIndex removing **/
    void remove();

    /** rebuild list of files to remove
     * (trim settings have been changed) **/
    void rescan();

    /** remove all tiles in selected directory **/
    void removeAll();
}
