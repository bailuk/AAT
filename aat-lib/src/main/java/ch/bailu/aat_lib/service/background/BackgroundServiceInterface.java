package ch.bailu.aat_lib.service.background;

import ch.bailu.aat_lib.util.WithStatusText;
import ch.bailu.foc.Foc;

public interface BackgroundServiceInterface extends WithStatusText {
    void close();

    void process(BackgroundTask backgroundTask);

    FileTask findTask(Foc resultFile);
}
