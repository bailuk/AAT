package ch.bailu.aat.activities;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.GPSStateButton;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.NumberView;
import ch.bailu.aat.views.TrackerStateButton;
import ch.bailu.aat.views.preferences.SolidIndexListView;


public class MainActivity extends AbsDispatcher
implements AdapterView.OnItemSelectedListener {

    private NumberView      gpsState;
    private TrackerStateButton trackerState;
    private ListView        actionList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViews();
        createDispatcher();

    }


    private void createViews() {
        LinearLayout contentView = new ContentView(this);
        contentView.addView(createButtonBar());


        contentView.addView(new SolidIndexListView(this, new SolidPreset(this)));

        actionList = createActionList();
        contentView.addView(actionList);

        setContentView(contentView);
    }


    private void createDispatcher() {
        DescriptionInterface[] target = new DescriptionInterface[] {
                gpsState, trackerState, this
        };

        ContentSource[] source = new ContentSource[] {
                new TrackerSource(getServiceContext()),
                new CurrentLocationSource(getServiceContext())
        };

        setDispatcher(new ContentDispatcher(this,source, target));
    }


    private ListView createActionList() {
        final MyListAdapter   actionAdapter = new MyListAdapter(this);
        actionList = new ListView(this);
        actionList.setOnItemClickListener(actionAdapter);
        actionList.setAdapter(actionAdapter);

        AppTheme.themify(actionList, Color.BLACK);

        return actionList;
    }


    private LinearLayout createButtonBar() {
        final ControlBar bar = new MainControlBar(getServiceContext());

        gpsState = new GPSStateButton(this);
        trackerState = new TrackerStateButton(getServiceContext());

        bar.addView(new View(this));
        bar.addView(gpsState);
        bar.addView(trackerState);

        return bar;

    }



    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
            long arg3) {
        new SolidPreset(this).setIndex(pos);
    }


    private class MyListAdapter extends BaseAdapter implements OnItemClickListener {
        private static final float MARGIN = 15f;
        private final int margin;

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


    @Override
    public void onServicesUp(boolean firstRun) {}

}
