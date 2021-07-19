package ch.bailu.aat.map;

import android.content.Context;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.map.MapContext;

public interface AndroidMapContext extends MapContext {
    ServiceContext getSContext();
    Context getContext();
}
