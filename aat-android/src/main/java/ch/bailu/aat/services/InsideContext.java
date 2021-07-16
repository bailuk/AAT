package ch.bailu.aat.services;

public abstract class InsideContext {

    public InsideContext(ServiceContext sc) {
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
