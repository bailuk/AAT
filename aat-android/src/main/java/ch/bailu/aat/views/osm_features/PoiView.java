package ch.bailu.aat.views.osm_features;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.mapsforge.poi.storage.PoiCategory;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.EditTextTool;
import ch.bailu.aat.views.preferences.SolidStringView;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.lib.filter_list.FilterList;
import ch.bailu.aat_lib.lib.filter_list.FilterListUtil;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidPoiDatabase;
import ch.bailu.aat_lib.preferences.SolidString;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.search.poi.PoiListItem;
import ch.bailu.foc.Foc;

public class PoiView extends LinearLayout implements OnPreferencesChanged {

    private final static String FILTER_KEY = PoiView.class.getSimpleName();

    private EditText filterView;
    private PoiListView listView;

    private final FilterList filterList = new FilterList();

    private final SolidPoiDatabase sdatabase;

    private final Foc selected;
    private final AppContext appContext;

    public PoiView(Context context, AppContext appContext, Foc s, UiTheme theme) {
        super(context);
        this.appContext = appContext;
        selected = s;

        sdatabase = new SolidPoiDatabase(appContext.getMapDirectory(), appContext);
        sdatabase.register(this);

        setOrientation(VERTICAL);
        addView(createHeader(theme));
        addView(createFilterView(theme));
        addView(createPoiList(context, theme));

        readList(appContext);
        filterList(filterView.getText().toString());
    }

    private void readList(AppContext appContext) {
        FilterListUtil.readList(filterList, appContext, sdatabase.getValueAsString(), selected);
        listView.onChanged();
    }

    public View createHeader(UiTheme theme) {
        return new SolidStringView(getContext(),sdatabase, theme);
    }

    private View createPoiList(Context context, UiTheme theme) {
        listView = new PoiListView(context, filterList, theme);
        listView.setOnTextSelected((e, action, variant) -> {
            if (e.isSummary()) {
                filterView.setText(e.getSummaryKey());

            } else {
                e.select();
                filterList.filterAll();
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
        filterList.filter(string);
        listView.onChanged();
    }


    public void close(ServiceContext sc) {
        saveSelected();

        new SolidString(new Storage(sc.getContext()), FILTER_KEY).setValue(filterView.getText().toString());
        sdatabase.unregister(this);
    }

    public ArrayList<PoiCategory> getSelectedCategories() {
        ArrayList<PoiCategory> export = new ArrayList<>(10);

        for (int i = 0; i< filterList.sizeVisible(); i++) {
            PoiListItem e = (PoiListItem) filterList.getFromVisible(i);

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
            readList(appContext);
        }
    }

    private void saveSelected() {
        saveSelected(selected);
    }

    public void saveSelected(Foc file) {
        try {
            FilterListUtil.writeSelected(filterList, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
