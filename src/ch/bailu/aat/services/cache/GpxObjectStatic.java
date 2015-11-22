package ch.bailu.aat.services.cache;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.util.SparseArray;
import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointLinkedNode;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.linked_list.Node;
import ch.bailu.aat.gpx.parser.GpxListReader;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.srtm.ElevationUpdaterClient;
import ch.bailu.aat.services.srtm.SRTM;
import ch.bailu.aat.services.srtm.SrtmAccess;

public class GpxObjectStatic extends GpxObject implements ElevationUpdaterClient {
    

    private GpxList gpxList=new GpxList(GpxBigDeltaInterface.TRK);

    private boolean ready=false;
    
    
    public GpxObjectStatic(String id, SelfOn self) {
        super(id);
        self.broadcaster.put(this);
    }

    
    @Override
    public void onInsert(SelfOn self) {
        reload(self);
    }
    

    private void reload(SelfOn self) {
        final FileHandle f = new FileHandle(toString()) {

            private boolean locked=false;
            
            @Override
            public long bgOnProcess() {
                locked=true;
                try {
                    GpxListReader reader = new GpxListReader(this, new File(toString()));
                    if (canContinue()) {
                        gpxList = reader.getGpxList();
                        ready=true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                locked=false;
                return getSize();
            }

            @Override
            public boolean isLocked() {
                return locked;
            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, GpxObjectStatic.this.toString());
                AppBroadcaster.broadcast(context, AppBroadcaster.REQUEST_ELEVATION_UPDATE, GpxObjectStatic.this.toString());
            }
        };

        self.background.load(f);        
    }

    public GpxObjectStatic() {
        super("");
    }


    @Override
    public boolean isReady() {
        return ready;
    }
    

    @Override
    public long getSize() {
        return gpxList.getPointList().size() * 
                (GpxPoint.SIZE_IN_BYTES + 
                        GpxPointLinkedNode.SIZE_IN_BYTES + 
                        Node.SIZE_IN_BYTES);
    }



    public GpxList getGpxList() {
        return gpxList;
    }


    public static class Factory extends ObjectHandle.Factory {

        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            return new GpxObjectStatic(id, self);
        }
    }



    @Override
    public void onDownloaded(String id, String url,  SelfOn self) {
        if (id.equals(toString())) {
            reload(self);
        }
        
    }

    @Override
    public void onChanged(String id, SelfOn self) {
        if (id.equals(toString())) self.iconMap.iconify(gpxList);
    }




    @Override
    public SrtmCoordinates[] getSrtmTileCoordinates() {
        
        SrtmTileCollector f = new SrtmTileCollector();
        f.walkTrack(gpxList);
        
        final SrtmCoordinates[] r=new SrtmCoordinates[f.coordinates.size()];
        for (int i=0; i<f.coordinates.size(); i++) {
            r[i]=f.coordinates.valueAt(i);
        }
        return r;
    }



    @Override
    public void updateFromSrtmTile(BackgroundService bg, SrtmAccess srtm) {
        new ListUpdater(srtm).walkTrack(gpxList);
        AppBroadcaster.broadcast(bg, AppBroadcaster.FILE_CHANGED_INCACHE, toString());
    }




    @Override
    public boolean isUpdating() {
        return false;
    }

    
    private class ListUpdater extends GpxListWalker {
        private final SrtmAccess srtmAccess;
    
        public ListUpdater(SrtmAccess s) {
            srtmAccess=s;
        }
    
        @Override
        public boolean doList(GpxList l) {
            return srtmAccess.isReady();
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return true;
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            if (point.getAltitude() == SRTM.NULL_ALTITUDE) {
                SrtmCoordinates coordinates = new SrtmCoordinates(point);
                if (srtmAccess.toString().equals(coordinates.toString())) {
                    point.setAltitude(srtmAccess.getElevation(point));
                }
            }
        }
    }
    
    private class SrtmTileCollector extends GpxListWalker {
        public final SparseArray<SrtmCoordinates> coordinates = new SparseArray<SrtmCoordinates>();
        
        @Override
        public boolean doList(GpxList l) {
            return true;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return true;
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            if (point.getAltitude() == SRTM.NULL_ALTITUDE) {
                final SrtmCoordinates c = new SrtmCoordinates(point);
                coordinates.put(c.toString().hashCode(), c);
            }
        }
    }    
}
