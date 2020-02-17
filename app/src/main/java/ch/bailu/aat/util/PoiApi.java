package ch.bailu.aat.util;

import android.content.Context;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.poi.android.storage.AndroidPoiPersistenceManagerFactory;
import org.mapsforge.poi.storage.ExactMatchPoiCategoryFilter;
import org.mapsforge.poi.storage.PoiCategory;
import org.mapsforge.poi.storage.PoiCategoryFilter;
import org.mapsforge.poi.storage.PoiPersistenceManager;
import org.mapsforge.poi.storage.PointOfInterest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.writer.WayWriter;
import ch.bailu.aat.gpx.writer.WayWriterOsmTags;
import ch.bailu.aat.preferences.map.SolidPoiDatabase;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.background.FileTask;
import ch.bailu.aat.test.GpxPointPoi;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;

public abstract class PoiApi extends OsmApiConfiguration {

    public final static String NAME = ToDo.translate("Offline POI");
    public final static String EXT=".gpx";
    public static final String SELECTED = "selected.txt";

    private final static int LIMIT = 10000;

    private final Foc directory;
    private final BoundingBoxE6 bounding;

    private BackgroundTask task = BackgroundTask.NULL;



    public PoiApi(Context context, BoundingBoxE6 box) {
        bounding = box;
        directory = AppDirectory.getDataDirectory(context, AppDirectory.DIR_POI);
    }


    @Override
    public String getApiName() {
        return NAME;
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
    public void startTask(ServiceContext scontext) {

        final ArrayList<PoiCategory> categories = getSelectedCategories();
        final String poiDatabase = new SolidPoiDatabase(scontext.getContext()).getValueAsString();

        new InsideContext(scontext) {
            @Override
            public void run() {
                task.stopProcessing();
                task = new PoiToGpxTask(getResultFile(),
                        bounding.toBoundingBox(),
                        categories,
                        poiDatabase);
                scontext.getBackgroundService().process(task);

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
        public long bgOnProcess(ServiceContext sc) {
            final PoiPersistenceManager persistenceManager =
                    AndroidPoiPersistenceManagerFactory.getPoiPersistenceManager(poiDatabase);

            try {
                queryPois(persistenceManager, bounding);
            } catch (Exception e) {
                setException(e);
            }

            persistenceManager.close();


            AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_ONDISK,
                    getFile().getPath(), poiDatabase);

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

            return persistenceManager.findInRect(box, categoryFilter, null, LIMIT);
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
