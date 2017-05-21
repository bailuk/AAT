package ch.bailu.aat.services.cache;

import android.content.Context;
import android.util.SparseArray;

import java.io.IOException;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointLinkedNode;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.linked_list.Node;
import ch.bailu.aat.gpx.parser.GpxListReader;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.services.dem.updater.ElevationUpdaterClient;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.UriAccess;

public class GpxObjectStatic extends GpxObject implements ElevationUpdaterClient {
    

    private GpxList gpxList=new GpxList(GpxType.TRK, new MaxSpeed.Samples());

    private boolean readyAndLoaded = false;
    
    
    public GpxObjectStatic(String id, ServiceContext sc) {
        super(id);
        sc.getCacheService().addToBroadcaster(this);
    }

    
    @Override
    public void onInsert(ServiceContext sc) {
        reload(sc);
    }
    

    private void reload(final ServiceContext sc) {
        final FileHandle f = new FileHandle(toString()) {


            @Override
            public long bgOnProcess() {
                long size = 0;

                ObjectHandle handle =
                        sc.getCacheService().getObject(GpxObjectStatic.this.toString());
                try {
                    if (handle instanceof GpxObjectStatic) {

                        final Context c = sc.getContext();
                        final String id = toString();

                        GpxListReader reader =
                                new GpxListReader(this, UriAccess.factory(c, id));
                        if (canContinue()) {
                            gpxList = reader.getGpxList();
                            readyAndLoaded = true;
                        }
                        size = getSize();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    handle.free();
                }
                return size;
            }


            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, GpxObjectStatic.this.toString());
                AppBroadcaster.broadcast(context, AppBroadcaster.REQUEST_ELEVATION_UPDATE, GpxObjectStatic.this.toString());
            }
        };
        sc.getBackgroundService().load(f);
    }


    public boolean isReadyAndLoaded() {
        return readyAndLoaded;
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
        public ObjectHandle factory(String id, ServiceContext sc) {
            return new GpxObjectStatic(id, sc);
        }
    }



    @Override
    public void onDownloaded(String id, String url,  ServiceContext sc) {
        if (id.equals(toString())) {
            reload(sc);
        }
    }

    @Override
    public void onChanged(String id, ServiceContext sc) {}


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
    public void updateFromSrtmTile(ServiceContext sc, Dem3Tile srtm) {
        new ListUpdater(srtm).walkTrack(gpxList);
        AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE, toString());
    }




    private class ListUpdater extends GpxListWalker {
        private final Dem3Tile tile;
        private SrtmCoordinates coordinates=new SrtmCoordinates(0,0);
        
        public ListUpdater(Dem3Tile s) {
            tile=s;
        }
    
        @Override
        public boolean doList(GpxList l) {
            return tile.isLoaded();
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
            if (point.getAltitude() == ElevationProvider.NULL_ALTITUDE) {
                coordinates=new SrtmCoordinates(point.getLatitudeE6(), point.getLongitudeE6());
                if (tile.hashCode()==coordinates.hashCode()) {
                    point.setAltitude(tile.getElevation(point.getLatitudeE6(), point.getLongitudeE6()));
                }
            }
        }
    }
    
    private class SrtmTileCollector extends GpxListWalker {
        public final SparseArray<SrtmCoordinates> coordinates = new SparseArray<>();
        
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
            if (point.getAltitude() == ElevationProvider.NULL_ALTITUDE) {
                final SrtmCoordinates c = new SrtmCoordinates(point);
                coordinates.put(c.toString().hashCode(), c);
            }
        }
    }    
}
