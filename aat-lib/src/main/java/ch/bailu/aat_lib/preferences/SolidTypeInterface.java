package ch.bailu.aat_lib.preferences;

import ch.bailu.aat_lib.description.ContentInterface;
import ch.bailu.aat_lib.util.ui.ToolTipProvider;

public interface SolidTypeInterface extends
        ContentInterface,
        ToolTipProvider {

    String getKey();
    StorageInterface getStorage();

    boolean hasKey(String key);

    void register(OnPreferencesChanged listener);
    void unregister(OnPreferencesChanged listener);
}
