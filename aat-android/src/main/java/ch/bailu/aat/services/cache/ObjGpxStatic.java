package ch.bailu.aat.services.cache;

import android.util.SparseArray;

import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileTask;
import ch.bailu.aat.services.elevation.tile.Dem3Status;
import ch.bailu.aat.services.elevation.tile.Dem3Tile;
import ch.bailu.aat.services.elevation.updater.ElevationUpdaterClient;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointLinkedNode;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.gpx.linked_list.Node;
import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.aat_lib.service.elevation.ElevationProvider;
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public final class ObjGpxStatic extends ObjGpx implements ElevationUpdaterClient {


    private GpxList gpxList = GpxList.NULL_TRACK;

    private boolean readyAndLoaded = false;

    private final Foc file;


    public ObjGpxStatic(String id, ServiceContext sc) {
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
                sc.getElevationService().cancelElevationUpdates(ObjGpxStatic.this);
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


    public static class Factory extends Obj.Factory {

        @Override
        public Obj factory(String id, ServiceContext sc) {
            return new ObjGpxStatic(id, sc);
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


    public Dem3Coordinates[] getSrtmTileCoordinates() {

        SrtmTileCollector f = new SrtmTileCollector();
        f.walkTrack(gpxList);

        final Dem3Coordinates[] r=new Dem3Coordinates[f.coordinates.size()];
        for (int i=0; i<f.coordinates.size(); i++) {
            r[i]=f.coordinates.valueAt(i);
        }
        return r;
    }



    @Override
    public void updateFromSrtmTile(ServiceContext sc, Dem3Tile srtm) {
        new ListUpdater(srtm).walkTrack(gpxList);

        OldAppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE, toString());

    }




    private class ListUpdater extends GpxListWalker {
        private final Dem3Tile tile;

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
                Dem3Coordinates coordinates = new Dem3Coordinates(point.getLatitudeE6(), point.getLongitudeE6());
                if (tile.hashCode()== coordinates.hashCode()) {
                    point.setAltitude(tile.getElevation(point.getLatitudeE6(), point.getLongitudeE6()));
                }
            }
        }
    }

    private class SrtmTileCollector extends GpxListWalker {
        public final SparseArray<Dem3Coordinates> coordinates = new SparseArray<>();

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
                final Dem3Coordinates c = new Dem3Coordinates(point);
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

            new OnObject(sc, getID(), ObjGpxStatic.class) {
                @Override
                public void run(Obj handle) {
                    ObjGpxStatic owner = (ObjGpxStatic) handle;

                    size[0] = load(sc, owner);

                    sc.getElevationService().requestElevationUpdates(owner,
                            owner.getSrtmTileCoordinates());

                    OldAppBroadcaster.broadcast(sc.getContext(),
                            AppBroadcaster.FILE_CHANGED_INCACHE, getID());

                }
            };

            return size[0];
        }


        private long load(ServiceContext sc, ObjGpxStatic handle) {
            long size = 0;

            GpxListReader reader = new GpxListReader(
                    getThreadControl(),
                    getFile(),
                    getAutoPause(sc, SolidPreset.getPresetFromFile(getFile())));

            handle.setException(reader.getException());

            if (canContinue()) {
                handle.setGpxList(reader.getGpxList());
                size = handle.getSize();
            }


            return size;
        }


        private AutoPause getAutoPause(ServiceContext sc, int preset) {
            SolidAutopause spause = new SolidPostprocessedAutopause(new Storage(sc.getContext()), preset);
            return new AutoPause.Time(
                    spause.getTriggerSpeed(),
                    spause.getTriggerLevelMillis());
        }
    }
}
