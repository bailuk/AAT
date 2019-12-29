package ch.bailu.aat.test;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.poi.android.storage.AndroidPoiPersistenceManagerFactory;
import org.mapsforge.poi.storage.ExactMatchPoiCategoryFilter;
import org.mapsforge.poi.storage.PoiCategory;
import org.mapsforge.poi.storage.PoiCategoryFilter;
import org.mapsforge.poi.storage.PoiCategoryManager;
import org.mapsforge.poi.storage.PoiPersistenceManager;
import org.mapsforge.poi.storage.PointOfInterest;
import org.mapsforge.poi.storage.UnknownPoiCategoryException;

import java.io.IOException;
import java.util.Collection;

import ch.bailu.aat.gpx.writer.WayWriter;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocFile;

public class PoiToGpx {

    private final static int LIMIT = 100;
    private final static String POI_FILE = "/storage/0B35-1209/maps/Alps/Alps_ML.poi";
    private final static Foc GPX_FILE =
            new FocFile("/storage/emulated/0/aat_data/overlay/poi_test.gpx");

    private final static String POI_CATEGORY = "restaurant";

    private final PoiPersistenceManager persistenceManager;


    public PoiToGpx() {

        persistenceManager =
                AndroidPoiPersistenceManagerFactory.getPoiPersistenceManager(POI_FILE);



        //persistenceManager.close();
    }



    public void queryPois(BoundingBox box) {
        try {
            final Collection<PointOfInterest> pois = searchPoi(box);

            if (pois != null) {
                if (GPX_FILE.exists()) GPX_FILE.remove();
                writeGpxFile(pois);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void writeGpxFile(Collection<PointOfInterest> pois) throws IOException {
        WayWriter writer = new WayWriter(GPX_FILE);

        writer.writeHeader(System.currentTimeMillis());



        for (PointOfInterest poi : pois) {
            writer.writeTrackPoint(new GpxPointPoi(poi));
        }
        writer.writeFooter();
        writer.close();
    }


    protected Collection<PointOfInterest> searchPoi(BoundingBox box) throws UnknownPoiCategoryException {
        final PoiCategoryManager categoryManager = persistenceManager.getCategoryManager();
        final PoiCategoryFilter categoryFilter = new ExactMatchPoiCategoryFilter();

        final PoiCategory root = categoryManager.getRootCategory();

        logCategory(root, "");
        categoryFilter.addCategory(findCategory(root, POI_CATEGORY));

        return persistenceManager.findInRect(box, categoryFilter, null, LIMIT);
    }


    public void close() {
        persistenceManager.close();
    }


    private void logCategory(PoiCategory category, String intent) {
        for (PoiCategory c: category.getChildren()) {
            AppLog.d(this, intent + c.getTitle());
            logCategory(c, "-" + intent);
        }
    }



    private PoiCategory findCategory(PoiCategory category, final String title)
            throws UnknownPoiCategoryException {



        for (PoiCategory c: category.getChildren()) {

            if (c.getTitle().toLowerCase().contains(title))
                return c;

            try {
                return findCategory(c, title);

            } catch (UnknownPoiCategoryException e) {

            }
        }
        throw new UnknownPoiCategoryException();
    }
}
