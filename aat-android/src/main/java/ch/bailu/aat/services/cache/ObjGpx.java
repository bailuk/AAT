package ch.bailu.aat.services.cache;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.gpx.GpxList;


public abstract class ObjGpx extends Obj {

    public ObjGpx(String id) {
        super(id);
    }

    public abstract GpxList getGpxList();


    public static final ObjGpx NULL = new ObjGpx(ObjGpx.class.getSimpleName()) {

        @Override
        public long getSize() {
            return MIN_SIZE;
        }

        @Override
        public void onChanged(String id, ServiceContext sc) {}

        @Override
        public void onDownloaded(String id, String url, ServiceContext sc) {}

        @Override
        public GpxList getGpxList() {
            return GpxList.NULL_TRACK;
        }

        @Override
        public boolean isReadyAndLoaded() {
            return true;
        }

    };
}
