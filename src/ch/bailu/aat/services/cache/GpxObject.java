package ch.bailu.aat.services.cache;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.services.cache.CacheService.SelfOn;


public abstract class GpxObject extends ObjectHandle {

    public GpxObject(String id) {
        super(id);
    }

    public abstract GpxList getGpxList();


    public static final GpxObject NULL = new GpxObject(GpxObject.class.getSimpleName()) {

        @Override
        public void onChanged(String id, SelfOn self) {}

        @Override
        public void onDownloaded(String id, String url, SelfOn self) {}

        @Override
        public GpxList getGpxList() {
            return GpxList.NULL_TRACK;
        }

    };
}
