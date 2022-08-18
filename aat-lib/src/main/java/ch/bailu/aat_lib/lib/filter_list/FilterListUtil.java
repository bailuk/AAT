package ch.bailu.aat_lib.lib.filter_list;

import org.mapsforge.poi.storage.PoiCategory;
import org.mapsforge.poi.storage.PoiCategoryManager;
import org.mapsforge.poi.storage.UnknownPoiCategoryException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.search.poi.PoiListItem;
import ch.bailu.foc.Foc;

public class FilterListUtil {
    private final static int LINE_LIMIT = 500;
    private final static int LINE_EST = 50;

    public static void writeSelected(FilterList filterList, Foc file) throws IOException {
        try (Writer out = new OutputStreamWriter(new BufferedOutputStream(file.openW()))) {
            for (int e = 0; e < filterList.sizeVisible(); e++) {
                if (filterList.getFromVisible(e).isSelected()) {

                    KeyList keys = filterList.getFromVisible(e).getKeys();

                    for (int k = 0; k < keys.size() - 1; k++) {
                        out.write(keys.getKey(k));
                        out.write(' ');
                    }
                    out.write('\n');
                }
            }
        }
    }

    public static void readSelected(FilterList filterList, Foc file) throws IOException {
        try (Reader in = new InputStreamReader(new BufferedInputStream(file.openR()))) {
            final StringBuilder builder = new StringBuilder(LINE_EST);

            int c = 0;
            while (c > -1) {
                c = in.read();

                if (c < 0 || c == '\n') {
                    final KeyList keys = new KeyList(builder.toString());

                    if (keys.size() > 0) {
                        select(filterList, keys);
                    }
                    builder.setLength(0);

                } else if (builder.length() < LINE_LIMIT) {
                    builder.append((char) c);
                }
            }
        }
    }

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
    private static void select(FilterList filterList, KeyList keys) {
        for(int e = 0; e<filterList.sizeAll(); e++) {
            if (filterList.getFromAll(e).getKeys().fits(keys)) {
                filterList.getFromAll(e).setSelected(true);
                break;
            }
        }
    }
}
