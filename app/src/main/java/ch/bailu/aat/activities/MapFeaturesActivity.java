package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.cache.osm_features.FeaturesListObject;
import ch.bailu.aat.services.cache.osm_features.ListData;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.filter_list.FilterList;
import ch.bailu.aat.util.filter_list.KeyList;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.MapFeaturesListView;
import ch.bailu.aat.views.bar.MainControlBar;


public class MapFeaturesActivity extends AbsDispatcher {
    private final static String FILTER_KEY="FilterView";

    private EditText filterView;
    private MapFeaturesListView listView;
    private FeaturesListObject listHandle = null;

    private final FilterList<ListData> list = new FilterList<ListData>() {
        @Override
        public boolean showElement(ListData e, KeyList keyList) {
            return e.keys.fits(keyList);
        }
    };


    private BroadcastReceiver onListLoaded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (AppIntent.hasFile(intent, FeaturesListObject.ID)) {
                updateList();
            }

        }
    };


    private void updateList() {
        if (listHandle == null) {
            list.clear();
        } else {
            listHandle.syncList(list);

        }
        listView.onChanged();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentView contentView = new ContentView(this, ContentView.VERTICAL);


        contentView.addView(createControlBar());
        contentView.addView(createFilterView());
        contentView.addView(createFeatureList());

        setContentView(contentView);

        AppBroadcaster.register(this, onListLoaded, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    private View createControlBar() {
        ControlBar bar = new MainControlBar(this, LinearLayout.HORIZONTAL, 6);
        bar.addIgnoreSize(AppTheme.getTitleTextView(this, R.string.query_features));
        return bar;
    }


    private View createFeatureList() {
        listView = new MapFeaturesListView(this, list);
        return listView;
    }

    private View createFilterView() {

        filterView = new EditText(this);
        filterView.setSingleLine(true);
        filterView.setText(new SolidString(this, FILTER_KEY ).getValueAsStringNonDef());
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
        return filterView;
    }



    @Override
    public void onDestroy() {
        new SolidString(this, FILTER_KEY).setValue(filterView.getText().toString());

        unregisterReceiver(onListLoaded);
        super.onDestroy();
    }


    @Override
    public void onPause() {
        if (listHandle != null) {
            listHandle.free();
            listHandle = null;
        }
        updateList();
        super.onPause();
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();

        new InsideContext(getServiceContext()) {

            @Override
            public void run() {
                listHandle = (FeaturesListObject)
                        getServiceContext().getCacheService().getObject(FeaturesListObject.ID, new FeaturesListObject.Factory());
            }
        };

        updateList();
    }
}
