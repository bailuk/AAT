package ch.bailu.aat.services.cache;

import ch.bailu.aat.services.ServiceContext;

public final class ObjNull extends Obj {

    public final static Obj NULL = new ObjNull();

    protected ObjNull() {
        super(ObjNull.class.getSimpleName());
    }

    @Override
    public long getSize() {
        return Obj.MIN_SIZE;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {

    }

    @Override
    public void onChanged(String id, ServiceContext sc) {

    }
}
