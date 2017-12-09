package ch.bailu.aat.services;

/**
 * Created by bailuk on 03.12.17.
 */

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
