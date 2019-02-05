package ch.bailu.aat.services.cache;

import ch.bailu.aat.services.ServiceContext;

public class NullHandle extends ObjectHandle {

    protected NullHandle() {
        super(NullHandle.class.getSimpleName());
    }

    @Override
    public long getSize() {
        return ObjectHandle.MIN_SIZE;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {

    }

    @Override
    public void onChanged(String id, ServiceContext sc) {

    }
}
