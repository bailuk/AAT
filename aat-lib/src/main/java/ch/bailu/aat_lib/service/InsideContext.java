package ch.bailu.aat_lib.service;

public abstract class InsideContext {

    public InsideContext(ServicesInterface sc) {
        if (sc.lock()) {
            try {
                run();
            }
            finally {
                sc.free();
            }
        }
    }

    public abstract void run();
}
