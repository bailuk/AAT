package ch.bailu.aat_lib.service.cache;

import ch.bailu.aat_lib.service.ServicesInterface;

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
    public void onDownloaded(String id, String url, ServicesInterface sc) {}

    @Override
    public void onChanged(String id, ServicesInterface sc) {}
}
