package ch.bailu.aat_lib.search.poi;

import org.mapsforge.poi.storage.PoiCategory;
import org.mapsforge.poi.storage.PoiCategoryManager;
import org.mapsforge.poi.storage.UnknownPoiCategoryException;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.lib.filter_list.FilterList;
import ch.bailu.aat_lib.lib.filter_list.FilterListUtil;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.foc.Foc;

public class FilterListUtilPoi {

    public static void readList(FilterList filterList, AppContext appContext, String db, Foc selected) {
        filterList.clear();

        var persistenceManager = appContext.getPoiPersistenceManager(db);
        var categoryManager = persistenceManager.getCategoryManager();

        try {
            readList(filterList, categoryManager);
            FilterListUtil.readSelected(filterList, selected);
        } catch (Exception e) {
            AppLog.d(selected, e.getMessage());
        }
        persistenceManager.close();
    }

    private static void readList(FilterList filterList, PoiCategoryManager categoryManager) throws UnknownPoiCategoryException {
        final PoiCategory root = categoryManager.getRootCategory();

        for (PoiCategory summary : root.getChildren()) {
            PoiListItem summaryEntry = new PoiListItem(summary);
            filterList.add(summaryEntry);

            for (PoiCategory category : summary.getChildren()) {
                filterList.add(new PoiListItem(category, summaryEntry));
            }
        }
    }
}
