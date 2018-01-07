package ch.bailu.aat.views.osm_features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidOsmFeaturesList;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.osm_features.MapFeaturesHandle;
import ch.bailu.aat.services.cache.osm_features.ListData;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.filter_list.FilterList;
import ch.bailu.aat.util.filter_list.KeyList;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.EditTextTool;
import ch.bailu.aat.views.preferences.SolidCheckBox;

public class MapFeaturesView extends LinearLayout implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String FILTER_KEY="FilterView";

    private EditText filterView;
    private MapFeaturesListView listView;
    private MapFeaturesHandle listHandle = null;

    private final BusyButton busyButton;

    private final SolidOsmFeaturesList slist;
    private final ServiceContext scontext;



    private final FilterList<ListData> list = new FilterList<ListData>() {
        @Override
        public boolean showElement(ListData e, KeyList keyList) {
            if (keyList.isEmpty()) {
                return e.isSummary();
            } else {
                return e.keys.fits(keyList);
            }
        }
    };


    private BroadcastReceiver onListLoaded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (listHandle != null && AppIntent.hasFile(intent, listHandle.getID())) {
                updateList();
            }
        }
    };


    private void updateList() {
        if (listHandle == null) {
            list.clear();
            busyButton.stopWaiting();
        } else {
            listHandle.syncList(list);
            if (listHandle.isReadyAndLoaded())
                busyButton.stopWaiting();
            else busyButton.startWaiting();
        }
        listView.onChanged();
    }


    public MapFeaturesView(ServiceContext sc, BusyButton b) {
        super(sc.getContext());
        scontext = sc;
        busyButton = b;
        slist = new SolidOsmFeaturesList(getContext());

        setOrientation(VERTICAL);
        addView(createHeader());
        addView(createFilterView());
        addView(createFeatureList());
    }


    public View createHeader() {
        return AppTheme.getTitleTextView(getContext(), R.string.query_features);
    }

    public void setOnTextSelected(MapFeaturesListView.OnSelected s) {
        listView.setOnTextSelected(s);
    }


    private View createFeatureList() {
        listView = new MapFeaturesListView(getContext(), list);
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

        EditTextTool layout = new EditTextTool(filterView);
        layout.add(new SolidCheckBox(slist));
        return layout;
    }





    public void onResume(final ServiceContext sc) {
        AppBroadcaster.register(sc.getContext(), onListLoaded, AppBroadcaster.FILE_CHANGED_INCACHE);

        filterView.setText(new SolidString(getContext(), FILTER_KEY ).getValueAsStringNonDef());

        getListHandle();

        slist.register(this);
    }

    public void onPause(ServiceContext sc) {
        slist.unregister(this);

        sc.getContext().unregisterReceiver(onListLoaded);

        freeListHandle();

        new SolidString(sc.getContext(), FILTER_KEY).setValue(filterView.getText().toString());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (slist.hasKey(s)) {
            freeListHandle();
            getListHandle();
        }
    }


    private void getListHandle() {
        new InsideContext(scontext) {

            @Override
            public void run() {
                listHandle = slist.getList(scontext.getCacheService());
            }
        };

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
