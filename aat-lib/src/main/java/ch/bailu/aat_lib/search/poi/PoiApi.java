package ch.bailu.aat_lib.search.poi;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.poi.storage.ExactMatchPoiCategoryFilter;
import org.mapsforge.poi.storage.PoiCategory;
import org.mapsforge.poi.storage.PoiCategoryFilter;
import org.mapsforge.poi.storage.PoiPersistenceManager;
import org.mapsforge.poi.storage.PointOfInterest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.preferences.SolidPoiDatabase;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.background.FileTask;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.aat_lib.xml.writer.WayWriter;
import ch.bailu.aat_lib.xml.writer.WayWriterOsmTags;
import ch.bailu.foc.Foc;

public abstract class PoiApi extends OsmApiConfiguration {

    public final static String EXT=".gpx";
    public static final String SELECTED = "selected.txt";

    private final static int LIMIT = 10000;

    private final Foc directory;
    private final BoundingBoxE6 bounding;

    private BackgroundTask task = BackgroundTask.NULL;


    public PoiApi(AppContext context, BoundingBoxE6 box) {
        bounding = box;
        directory = AppDirectory.getDataDirectory(context.getDataDirectory(), AppDirectory.DIR_POI);
    }

    @Override
    public String getApiName() {
        return ch.bailu.aat_lib.resources.Res.str().p_mapsforge_poi();
    }

    @Override
    public String getUrl(String query) {
        return "";
    }

    @Override
    public String getUrlStart() {
        return "";
    }

    @Override
    public Foc getBaseDirectory() {
        return directory;
    }

    @Override
    public String getFileExtension() {
        return EXT;
    }

    @Override
    public String getUrlPreview(String s) {
        return "";
    }

    @Override
    public void startTask(AppContext appContext) {

        final ArrayList<PoiCategory> categories = getSelectedCategories();
        final String poiDatabase = new SolidPoiDatabase(appContext.getMapDirectory(), appContext).getValueAsString();

        new InsideContext(appContext.getServices()) {
            @Override
            public void run() {
                task.stopProcessing();
                task = new PoiToGpxTask(getResultFile(),
                        bounding.toBoundingBox(),
                        categories,
                        poiDatabase);
                appContext.getServices().getBackgroundService().process(task);
            }
        };
    }

    protected abstract ArrayList<PoiCategory> getSelectedCategories();

    private static class PoiToGpxTask extends FileTask {
        private final BoundingBox bounding;
        private final String poiDatabase;
        private final ArrayList<PoiCategory> categories;

        public PoiToGpxTask(Foc result,
                            BoundingBox b,
                            ArrayList<PoiCategory> c,
                            String poiDb) {
            super(result);
            poiDatabase = poiDb;
            bounding = b;
            categories = c;
        }

        @Override
        public long bgOnProcess(AppContext sc) {
            final PoiPersistenceManager persistenceManager = sc.getPoiPersistenceManager(poiDatabase);

            try {
                queryPois(persistenceManager, bounding);
            } catch (Exception e) {
                setException(e);
            }

            persistenceManager.close();

            sc.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_ONDISK,
                    getFile().toString(), poiDatabase);

            return 100;
        }

        private void queryPois(PoiPersistenceManager persistenceManager, BoundingBox box) throws IOException {
            final Collection<PointOfInterest> pois = searchPoi(persistenceManager, box);

            if (pois != null) {
                if (getFile().exists()) getFile().remove();
                writeGpxFile(pois);
            }
        }

        private Collection<PointOfInterest> searchPoi(PoiPersistenceManager persistenceManager,
                                                      BoundingBox box) {
            final PoiCategoryFilter categoryFilter = new ExactMatchPoiCategoryFilter();

            for (PoiCategory category : categories)
                categoryFilter.addCategory(category);

            return persistenceManager.findInRect(box, categoryFilter, null, box.getCenterPoint(), LIMIT);
        }

        private void writeGpxFile(Collection<PointOfInterest> pois) throws IOException {
            WayWriter writer = new WayWriterOsmTags(getFile());

            writer.writeHeader(System.currentTimeMillis());

            for (PointOfInterest poi : pois) {
                writer.writeTrackPoint(new GpxPointPoi(poi));
            }
            writer.writeFooter();
            writer.close();
        }
    }

    @Override
    public Exception getException() {
        return task.getException();
    }

}
