package ch.bailu.aat.services.background;

import android.support.annotation.NonNull;

import ch.bailu.util_java.foc.Foc;

public abstract class FileTask extends BackgroundTask {
    private final Foc file;



    public FileTask(Foc f) {
        file = f;
    }


    @NonNull
    @Override
    public String toString() {
        return file.toString();
    }

    public Foc getFile() {
        return file;
    }

    public String getID() {
        return file.getPath();
    }
}
