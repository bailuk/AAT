package ch.bailu.aat_lib.service.background;

import javax.annotation.Nonnull;

import ch.bailu.foc.Foc;

public abstract class FileTask extends BackgroundTask {
    private final Foc file;

    private Tasks tasks = null;


    public FileTask(Foc f) {
        file = f;
    }


    @Nonnull
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


    public void register(Tasks t) {
        if (tasks == null)
            tasks = t;
    }


    @Override
    public void onInsert() {
        if (tasks != null) tasks.add(this);
    }


    @Override
    public void onRemove() {
        if (tasks != null) {
            tasks.remove(this);
        }
    }
}
