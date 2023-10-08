package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.preferences.SolidFileInterface;
import ch.bailu.aat_lib.preferences.SolidTypeInterface;

public interface SolidOverlayInterface extends SolidTypeInterface, SolidFileInterface {
    boolean isEnabled();
    void setEnabled(boolean enabled);
    int getIID();
}
