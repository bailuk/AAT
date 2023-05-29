package ch.bailu.aat_lib.dispatcher;

public interface FileSourceInterface extends ContentSourceInterface {
    void setFileID(String fileID);
    boolean isEnabled();
    void setEnabled(boolean lifeCycleEnabled);
}
