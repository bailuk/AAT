package ch.bailu.aat.services;

import android.content.Context;

import java.io.Closeable;

public abstract class VirtualService implements Closeable {


    private final ServiceContext scontext;

    public VirtualService(ServiceContext sc) {
        scontext = sc;
    }

    public abstract void appendStatusText(StringBuilder builder);

    protected final ServiceContext getSContext() {
        return scontext;
    }
    protected final Context getContext() {
        return scontext.getContext();
    }



}
