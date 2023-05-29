package ch.bailu.aat_lib.dispatcher;

import ch.bailu.foc.Foc;

public interface FileSourceInterface extends ContentSourceInterface {
    void setFile(Foc file);
    boolean isEnabled();
    void setEnabled(boolean lifeCycleEnabled);
}
