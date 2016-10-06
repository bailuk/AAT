package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.RootDispatcher;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.views.AbsLabelTextView;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.GPSStateButton;
import ch.bailu.aat.views.description.NumberView;
import ch.bailu.aat.views.description.TrackerStateButton;
import ch.bailu.aat.views.preferences.SolidIndexListView;
import ch.bailu.aat.views.preferences.VerticalScrollView;


public class MainActivity extends AbsDispatcher {

    private NumberView      gpsState;
    private TrackerStateButton trackerState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViews();
        createDispatcher();

    }


    private void createViews() {

        LinearLayout contentView = new ContentView(this);

        contentView.addView(createButtonBar());
        contentView.addView(createActionList());

        setContentView(contentView);
    }


    private View createActionList() {

        final VerticalScrollView list = new VerticalScrollView(this);
        list.add(new SolidIndexListView(new SolidPreset(this)));

        for (int i = 0; i < ActivitySwitcher.list.length; i++) {
            list.add(new ActivityLabel(ActivitySwitcher.list[i]));
        }
        return list;
    }


    private void createDispatcher() {
        DescriptionInterface[] target = new DescriptionInterface[] {
                gpsState, trackerState, this
        };

        ContentSource[] source = new ContentSource[] {
                new TrackerSource(getServiceContext()),
                new CurrentLocationSource(getServiceContext())
        };

        setDispatcher(new RootDispatcher(this,source, target));
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


    private class ActivityLabel extends AbsLabelTextView {
        public ActivityLabel(final ActivitySwitcher s) {
            super(MainActivity.this, getString(s.getLabel()));

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.start(MainActivity.this);
                }
            });
        }
    }
}
