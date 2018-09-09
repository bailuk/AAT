package ch.bailu.aat.services.cache;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.services.ServiceContext;


public abstract class GpxObject extends ObjectHandle {

    public GpxObject(String id) {
        super(id);
    }

    public abstract GpxList getGpxList();


    public static final GpxObject NULL = new GpxObject(GpxObject.class.getSimpleName()) {

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
