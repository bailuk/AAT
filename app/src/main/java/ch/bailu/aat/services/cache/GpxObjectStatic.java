package ch.bailu.aat.services.cache;

import android.util.SparseArray;

import java.io.IOException;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointLinkedNode;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.attributes.AutoPause;
import ch.bailu.aat.gpx.linked_list.Node;
import ch.bailu.aat.gpx.xml_parser.GpxListReader;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileTask;
import ch.bailu.aat.services.dem.tile.Dem3Status;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.services.dem.updater.ElevationUpdaterClient;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

public class GpxObjectStatic extends GpxObject implements ElevationUpdaterClient {


    private GpxList gpxList = GpxList.NULL_TRACK;

    private boolean readyAndLoaded = false;

    private final Foc file;


    public GpxObjectStatic(String id, ServiceContext sc) {
        super(id);
        sc.getCacheService().addToBroadcaster(this);

        file = FocAndroid.factory(sc.getContext(), id);
    }


    @Override
    public void onInsert(ServiceContext sc) {
        reload(sc);
    }


    @Override
    public void onRemove(final ServiceContext sc) {
        new InsideContext(sc) {
            @Override
            public void run() {
                sc.getElevationService().cancelElevationUpdates(GpxObjectStatic.this);
            }
        };
    }


    @Override
    public Foc getFile() {
        return file;
    }

    private void reload(final ServiceContext sc) {
        sc.getBackgroundService().process(new FileLoader(file));
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



    private void setGpxList(GpxList list) {
        readyAndLoaded = true;
        gpxList = list;
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
        if (id.equals(getID())) {
            reload(sc);
        }
    }

    @Override
    public void onChanged(String id, ServiceContext sc) {}


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
            return tile.getStatus() == Dem3Status.VALID;
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


    private static class FileLoader extends FileTask {

        public FileLoader(Foc f) {
            super(f);

        }

        @Override
        public long bgOnProcess(final ServiceContext sc) {
            final long[] size = {0};

            new OnObject(sc, getID(), GpxObjectStatic.class) {
                @Override
                public void run(ObjectHandle handle) {
                    GpxObjectStatic owner = (GpxObjectStatic) handle;

                    size[0] = load(sc, owner);

                    sc.getElevationService().requestElevationUpdates(owner,
                            owner.getSrtmTileCoordinates());

                    AppBroadcaster.broadcast(sc.getContext(),
                            AppBroadcaster.FILE_CHANGED_INCACHE, getID());

                }
            };

            return size[0];
        }


        private long load(ServiceContext sc, GpxObjectStatic handle) {
            long size = 0;

            try {
                GpxListReader reader = new GpxListReader(
                        getThreadControl(),
                        getFile(),
                        getAutoPause(sc));

                if (canContinue()) {
                    handle.setGpxList(reader.getGpxList());
                    handle.setException(reader.getParserException());

                    size = handle.getSize();
                }

            } catch (IOException | IllegalArgumentException | SecurityException e) {
                handle.setException(e);
            }

            return size;
        }


        private AutoPause getAutoPause(ServiceContext sc) {
            SolidAutopause spause = new SolidPostprocessedAutopause(sc.getContext());
            return new AutoPause.Time(
                    spause.getTriggerSpeed(),
                    spause.getTriggerLevelMillis());
        }
    }
}
