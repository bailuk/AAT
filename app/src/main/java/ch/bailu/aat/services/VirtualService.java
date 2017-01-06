package ch.bailu.aat.services;

import android.content.Context;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxAttributes;

public abstract class VirtualService implements Closeable {
    
    
    private final ServiceContext scontext;
    
    public VirtualService(ServiceContext sc) {
        scontext = sc;
    }

    public abstract void appendStatusText(StringBuilder builder);

    public ServiceContext getSContext() {
        return scontext;
    }
    public Context getContext() {
        return scontext.getContext();
    }


}
