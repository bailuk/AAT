package ch.bailu.aat_lib.service.cache;

import ch.bailu.aat_lib.app.AppContext;


public abstract class OnObject {
    public OnObject(final AppContext appContext, final String id, final Class c) {
        this(appContext, id, c, null);
    }


    public OnObject(final AppContext appContext, final String id, final Class c,
                    final Obj.Factory factory) {

        appContext.getServices().insideContext(() -> {
            Obj handle;

            if (factory == null)
                handle = appContext.getServices().getCacheService().getObject(id);

            else
                handle = appContext.getServices().getCacheService().getObject(id, factory);

            try {
                if (c.isInstance(handle))
                    OnObject.this.run(handle);

            } finally {
                handle.free();
            }
        });
    }

    public abstract void run(Obj handle);
}
