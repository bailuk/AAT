package ch.bailu.aat.views.osm_features;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.mapsforge.poi.android.storage.AndroidPoiPersistenceManagerFactory;
import org.mapsforge.poi.storage.PoiCategory;
import org.mapsforge.poi.storage.PoiCategoryManager;
import org.mapsforge.poi.storage.PoiPersistenceManager;
import org.mapsforge.poi.storage.UnknownPoiCategoryException;

import java.util.ArrayList;

import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.filter_list.FilterList;
import ch.bailu.aat.util.filter_list.ListEntry;
import ch.bailu.aat.util.filter_list.PoiListEntry;
import ch.bailu.aat.views.EditTextTool;
import ch.bailu.aat.views.preferences.TitleView;

public class PoiView  extends LinearLayout {

    private final static String POI_FILE = "/storage/0B35-1209/maps/Alps/Alps_ML.poi";
    private final static String FILTER_KEY = "PoiView";

    private EditText filterView;
    private PoiListView listView;

    private final ServiceContext scontext;

    private final FilterList list = new FilterList();


    public PoiView(ServiceContext sc) {
        super(sc.getContext());
        scontext = sc;

        setOrientation(VERTICAL);
        addView(createHeader());
        addView(createFilterView());
        addView(createPoiList());

        readList();
    }


    private void readList() {
        list.clear();


        final PoiPersistenceManager persistenceManager =
                AndroidPoiPersistenceManagerFactory.getPoiPersistenceManager(POI_FILE);

        final PoiCategoryManager categoryManager = persistenceManager.getCategoryManager();

        try {
            readList(categoryManager);
        } catch (UnknownPoiCategoryException e) {
            e.printStackTrace();
        }

        persistenceManager.close();
        listView.onChanged();
    }

    private void readList(PoiCategoryManager categoryManager) throws UnknownPoiCategoryException {
        final PoiCategory root = categoryManager.getRootCategory();

        for (PoiCategory summary : root.getChildren()) {
            PoiListEntry summaryEntry = new PoiListEntry(summary);
            list.add(summaryEntry);

            for (PoiCategory category : summary.getChildren()) {
                list.add(new PoiListEntry(category, summaryEntry));
            }
        }

    }

    public View createHeader() {

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(HORIZONTAL);
        layout.addView(new TitleView(getContext(), POI_FILE));

        return layout;
    }


    private View createPoiList() {
        listView = new PoiListView(scontext, list);
        listView.setOnTextSelected((e, action, variant) -> {
            if (e.isSummary()) {
                filterView.setText(e.getSummaryKey());

            } else {
                e.select();
                list.filterAll();
                listView.onChanged();
            }
        });

        return listView;
    }


    private View createFilterView() {
        filterView = new EditText(getContext());
        filterView.setSingleLine(true);
        filterView.setText(new SolidString(getContext(), FILTER_KEY).getValueAsStringNonDef());
        filterView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.filter(charSequence.toString());
                listView.onChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditTextTool layout = new EditTextTool(filterView, LinearLayout.VERTICAL);
        return layout;
    }


    public void onPause(ServiceContext sc) {
        new SolidString(sc.getContext(), FILTER_KEY).setValue(filterView.getText().toString());
    }

    public ArrayList<PoiCategory> getCategories() {
        ArrayList<PoiCategory> export = new ArrayList<>(10);

        for (int i = 0; i< list.sizeVisible(); i++) {
            PoiListEntry e = (PoiListEntry) list.get(i);

            if (e.isSelected()) {
                export.add(e.getCategory());
            }
        }
        return export;
    }
}
