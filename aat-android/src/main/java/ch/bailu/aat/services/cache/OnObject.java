package ch.bailu.aat.services.cache;

import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;


public abstract class OnObject {
    public OnObject(final ServiceContext sc, final String id, final Class c) {
        this (sc, id, c, null);
    }


    public OnObject(final ServiceContext sc, final String id, final Class c,
                    final Obj.Factory factory) {

        new InsideContext(sc) {
            @Override
            public void run() {
                Obj handle;

                if (factory == null)
                    handle = sc.getCacheService().getObject(id);

                else
                    handle = sc.getCacheService().getObject(id, factory);

                try {
                    if (c.isInstance(handle))
                        OnObject.this.run(handle);

                } finally {
                    handle.free();
                }
            }
        };
    }

    public abstract void run(Obj handle);
}
