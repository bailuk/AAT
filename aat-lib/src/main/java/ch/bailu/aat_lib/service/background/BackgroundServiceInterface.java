package ch.bailu.aat_lib.service.background;

import ch.bailu.aat_lib.util.WithStatusText;

public interface BackgroundServiceInterface extends WithStatusText {
    void close();

    void process(BackgroundTask backgroundTask);
}
