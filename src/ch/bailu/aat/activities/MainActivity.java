package ch.bailu.aat.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.description.GpsStateDescription;
import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.tracker.TrackerService;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.NumberView;



public class MainActivity extends AbsDispatcher 
implements AdapterView.OnItemSelectedListener, OnSharedPreferenceChangeListener {
    
    
    private static final Class<?> SERVICES[] = {
        TrackerService.class,
        CacheService.class,
    };
    
    
    private NumberView      gpsState, trackerState;
    private LinearLayout    contentView;
    private Spinner         presetSpinner; 
    private ListView        actionList;
    private MyListAdapter   actionAdapter;

    private Storage storage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = Storage.global(this);
        storage.register(this);

        contentView=new ContentView(this);
        contentView.addView(createButtonBar());

        presetSpinner = createActivitySpinner();
        contentView.addView(presetSpinner);
        actionList = createActionList();
        contentView.addView(actionList);

        setContentView(contentView);

        connectToServices(SERVICES);
    }


    private ListView createActionList() {
        actionAdapter = new MyListAdapter(this);
        actionList = new ListView(this);
        
        actionList.setOnItemClickListener(actionAdapter);
        actionList.setAdapter(actionAdapter);
        
        AppTheme.themify(actionList, Color.BLACK);

        return actionList;
    }


    private LinearLayout createButtonBar() {
        LinearLayout bar = new LinearLayout(this);
        int height = AppTheme.getBigButtonSize(this);
        int width = AppTheme.getBigButtonSize(this)*2;

        gpsState = new NumberView(new GpsStateDescription(this),
                GpxInformation.ID.INFO_ID_LOCATION);
        trackerState = new NumberView(new TrackerStateDescription(this), 
                GpxInformation.ID.INFO_ID_TRACKER);

        bar.addView(gpsState, width,height);
        bar.addView(trackerState, width,height);

        bar.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
            
        });
        AppTheme.themify(bar);
        return bar;
        
    }

    
    private Spinner createActivitySpinner() {
        Spinner spinner = new Spinner(this);
        initializeSpinner(spinner);

        return spinner;

    }

    private void initializeSpinner(Spinner spinner) {
        SolidPreset spreset = new SolidPreset(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_spinner_item, 
                spreset.getStringArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setPromptId(R.string.p_preset);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(spreset.getIndex());
        spinner.setOnItemSelectedListener(this);
    }




    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
            long arg3) {
        new SolidPreset(this).setIndex(pos);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) { 
        initializeSpinner(presetSpinner);
    }




    @Override
    public void onDestroy() {
        storage.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onServicesUp() {
        try {
            DescriptionInterface[] target = new DescriptionInterface[] {
                    gpsState, trackerState, this
            };

            ContentSource[] source = new ContentSource[] {
                    new TrackerSource(getServiceContext().getTrackerService()),
                    new CurrentLocationSource(getServiceContext().getTrackerService())
            };

            setDispatcher(new ContentDispatcher(this,source, target));
        } catch (ServiceNotUpException e) {
            AppLog.e(this, e);
        }
    }

    private class MyListAdapter extends BaseAdapter implements OnItemClickListener {
        private static final float MARGIN = 15f;
        private int margin;

        public MyListAdapter(Context context) {
            margin=(int)AppLayout.toPixel(context, MARGIN);
        }

        @Override
        public int getCount() {
            return ActivitySwitcher.list.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label;

            if (convertView != null) label = (TextView)convertView;
            else label = new TextView(MainActivity.this);

            label.setText(ActivitySwitcher.list[position].getLabel());
            AppTheme.themify(label);

            label.setPadding(margin,margin,margin,margin);
            return label;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                long arg3) {
            ActivitySwitcher.list[pos].start(MainActivity.this);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {}


}
