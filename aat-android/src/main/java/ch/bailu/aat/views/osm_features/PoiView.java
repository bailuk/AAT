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

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidPoiDatabase;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.filter_list.FilterList;
import ch.bailu.aat.util.filter_list.FilterListUtil;
import ch.bailu.aat.util.filter_list.PoiListEntry;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.EditTextTool;
import ch.bailu.aat.views.preferences.SolidStringView;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidString;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;

public class PoiView  extends LinearLayout implements OnPreferencesChanged {

    private final static String FILTER_KEY = PoiView.class.getSimpleName();

    private EditText filterView;
    private PoiListView listView;

    private final ServiceContext scontext;

    private final FilterList list = new FilterList();

    private final SolidPoiDatabase sdatabase;

    private final Foc selected;

    public PoiView(ServiceContext sc, SolidMapsForgeDirectory smap, FocFactory focFactory, Foc s, UiTheme theme) {
        super(sc.getContext());
        scontext = sc;

        selected = s;

        sdatabase = new SolidPoiDatabase(smap, focFactory);
        sdatabase.register(this);

        setOrientation(VERTICAL);
        addView(createHeader(theme));
        addView(createFilterView(theme));
        addView(createPoiList(theme));

        readList();
        filterList(filterView.getText().toString());
    }


    private void readList() {
        list.clear();

        final PoiPersistenceManager persistenceManager =
                AndroidPoiPersistenceManagerFactory.getPoiPersistenceManager(
                        sdatabase.getValueAsString());

        final PoiCategoryManager categoryManager = persistenceManager.getCategoryManager();

        try {
            readList(categoryManager);
            readSelected();

        } catch (Exception e) {
            e.printStackTrace();
        }

        persistenceManager.close();
        listView.onChanged();
    }

    private void readSelected() throws IOException {
        new FilterListUtil(list).readSelected(selected);
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

    public View createHeader(UiTheme theme) {
        return new SolidStringView(getContext(),sdatabase, theme);
    }


    private View createPoiList(UiTheme theme) {
        listView = new PoiListView(scontext, list, theme);
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


    private View createFilterView(UiTheme theme) {
        filterView = new EditText(getContext());
        filterView.setSingleLine(true);
        filterView.setText(new SolidString(new Storage(getContext()), FILTER_KEY).getValueAsStringNonDef());
        filterView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return new EditTextTool(filterView, LinearLayout.VERTICAL, theme);
    }

    private void filterList(String string) {
        list.filter(string);
        listView.onChanged();
    }


    public void close(ServiceContext sc) {

        saveSelected();

        new SolidString(new Storage(sc.getContext()), FILTER_KEY).setValue(filterView.getText().toString());
        sdatabase.unregister(this);
    }


    public ArrayList<PoiCategory> getSelectedCategories() {
        ArrayList<PoiCategory> export = new ArrayList<>(10);

        for (int i = 0; i< list.sizeVisible(); i++) {
            PoiListEntry e = (PoiListEntry) list.getFromVisible(i);

            if (e.isSelected()) {
                export.add(e.getCategory());
            }
        }
        return export;
    }

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
        if (sdatabase.hasKey(key)) {
            saveSelected();
            readList();
        }
    }

    private void saveSelected() {
        saveSelected(selected);
    }

    public void saveSelected(Foc file) {
        try {
            new FilterListUtil(list).writeSelected(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
