package ch.bailu.aat.views.osm_features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import javax.annotation.Nonnull;

import ch.bailu.aat.dispatcher.AndroidBroadcaster;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.SolidOsmFeaturesList;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.osm_features.ObjMapFeatures;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.BusyIndicator;
import ch.bailu.aat.views.EditTextTool;
import ch.bailu.aat.views.preferences.SolidCheckBox;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.lib.filter_list.FilterList;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidString;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class OsmFeaturesView extends LinearLayout implements OnPreferencesChanged {

    private final static String FILTER_KEY="FilterView";

    private EditText filterView;
    private MapFeaturesListView listView;
    private ObjMapFeatures listHandle = null;

    private final BusyIndicator busy;

    private final SolidOsmFeaturesList slist;
    private final ServiceContext scontext;

    private final FilterList list = new FilterList();

    private final UiTheme theme = AppTheme.search;

    private final BroadcastReceiver onListLoaded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (listHandle != null && AppIntent.hasFile(intent, listHandle.getID())) {
                updateList();
            }
        }
    };

    public OsmFeaturesView(ServiceContext sc) {
        super(sc.getContext());
        scontext = sc;

        busy = new BusyIndicator(getContext());
        slist = new SolidOsmFeaturesList(getContext());

        setOrientation(VERTICAL);
        //addView(createHeader());
        addView(createFilterView());
        addView(createFeatureList());
    }

    private void updateList() {
        if (listHandle == null) {
            list.clear();
            busy.stopWaiting();
        } else {
            listHandle.syncList(list);
            if (listHandle.isReadyAndLoaded())
                busy.stopWaiting();
            else busy.startWaiting();
        }
        listView.onChanged();
    }

    public void setOnTextSelected(OnSelected s) {
        listView.setOnTextSelected(s);
    }

    private View createFeatureList() {
        listView = new MapFeaturesListView(scontext, list);
        return listView;
    }

    private View createFilterView() {
        filterView = new EditText(getContext());
        filterView.setSingleLine(true);
        filterView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.filter(charSequence.toString());
                updateList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditTextTool layout = new EditTextTool(filterView, LinearLayout.VERTICAL, theme);
        layout.add(new SolidCheckBox(getContext(),slist, theme));
        return layout;
    }

    public void onResume(final ServiceContext sc) {
        AndroidBroadcaster.register(sc.getContext(), onListLoaded, AppBroadcaster.FILE_CHANGED_INCACHE);

        filterView.setText(new SolidString(new Storage(getContext()), FILTER_KEY ).getValueAsStringNonDef());

        getListHandle();

        slist.register(this);
    }

    public void onPause(ServiceContext sc) {
        slist.unregister(this);

        sc.getContext().unregisterReceiver(onListLoaded);

        freeListHandle();

        new SolidString(new Storage(sc.getContext()), FILTER_KEY).setValue(filterView.getText().toString());
    }

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {

        if (slist.hasKey(key)) {
            freeListHandle();
            getListHandle();
        }
    }

    private void getListHandle() {
        scontext.insideContext(()-> listHandle = slist.getList(scontext.getCacheService()));
        updateList();
    }

    private void freeListHandle() {
        if (listHandle != null) {
            listHandle.free();
            listHandle = null;
        }

        updateList();
    }

    public void setFilterText(String summaryKey) {
        filterView.setText(summaryKey);
    }
}
