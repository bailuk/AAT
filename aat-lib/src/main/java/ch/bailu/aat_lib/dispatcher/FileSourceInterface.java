package ch.bailu.aat_lib.dispatcher;

public interface FileSourceInterface extends ContentSourceInterface {
    void setFileID(String fileID);
    void enable();
    void disable();
}
