package ch.bailu.aat_lib.service.cache;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointLinkedNode;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.gpx.linked_list.Node;
import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.background.FileTask;
import ch.bailu.aat_lib.service.elevation.Dem3Status;
import ch.bailu.aat_lib.service.elevation.ElevationProvider;
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile;
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient;
import ch.bailu.aat_lib.util.IndexedMap;
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader;
import ch.bailu.foc.Foc;

public final class ObjGpxStatic extends ObjGpx implements ElevationUpdaterClient {


    private GpxList gpxList = GpxList.NULL_TRACK;

    private boolean readyAndLoaded = false;

    private final Foc file;


    public ObjGpxStatic(String id, AppContext appContext) {
        super(id);
        appContext.getServices().getCacheService().addToBroadcaster(this);

        file = appContext.toFoc(id);
    }


    @Override
    public void onInsert(AppContext appContext) {
        reload(appContext);
    }


    @Override
    public void onRemove(final AppContext appContext) {
        new InsideContext(appContext.getServices()) {
            @Override
            public void run() {
                appContext.getServices().getElevationService().cancelElevationUpdates(ObjGpxStatic.this);
            }
        };
    }


    @Override
    public Foc getFile() {
        return file;
    }

    private void reload(final AppContext appContext) {
        appContext.getServices().getBackgroundService().process(new FileLoader(file));
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
        public Obj factory(String id, AppContext appContext) {
            return new ObjGpxStatic(id, appContext);
        }
    }



    @Override
    public void onDownloaded(String id, String url,  AppContext appContext) {
        if (id.equals(getID())) {
            reload(appContext);
        }
    }

    @Override
    public void onChanged(String id, AppContext appContext) {}


    public Dem3Coordinates[] getSrtmTileCoordinates() {

        SrtmTileCollector f = new SrtmTileCollector();
        f.walkTrack(gpxList);

        final Dem3Coordinates[] r=new Dem3Coordinates[f.coordinates.size()];
        for (int i=0; i<f.coordinates.size(); i++) {
            r[i]=f.coordinates.getValueAt(i);
        }
        return r;
    }



    @Override
    public void updateFromSrtmTile(AppContext appContext, Dem3Tile srtm) {
        new ListUpdater(srtm).walkTrack(gpxList);

        appContext.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, toString());

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

        public final IndexedMap<String, Dem3Coordinates> coordinates = new IndexedMap<>();

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
                coordinates.put(c.toString(), c);
            }
        }
    }


    private static class FileLoader extends FileTask {

        public FileLoader(Foc f) {
            super(f);

        }

        @Override
        public long bgOnProcess(final AppContext appContext) {
            final long[] size = {0};

            new OnObject(appContext, getID(), ObjGpxStatic.class) {
                @Override
                public void run(Obj handle) {
                    ObjGpxStatic owner = (ObjGpxStatic) handle;

                    size[0] = load(appContext, owner);

                    appContext.getServices().getElevationService().requestElevationUpdates(owner,
                            owner.getSrtmTileCoordinates());

                    appContext.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, getID());

                }
            };

            return size[0];
        }


        private long load(AppContext appContext, ObjGpxStatic handle) {
            long size = 0;

            GpxListReader reader = new GpxListReader(
                    getThreadControl(),
                    getFile(),
                    getAutoPause(appContext, SolidPreset.getPresetFromFile(getFile())));

            handle.setException(reader.getException());

            if (canContinue()) {
                handle.setGpxList(reader.getGpxList());
                size = handle.getSize();
            }


            return size;
        }


        private AutoPause getAutoPause(AppContext appContext, int preset) {
            SolidAutopause spause = new SolidPostprocessedAutopause(appContext.getStorage(), preset);
            return new AutoPause.Time(
                    spause.getTriggerSpeed(),
                    spause.getTriggerLevelMillis());
        }
    }
}
