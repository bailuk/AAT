package ch.bailu.aat.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.preferences.SolidDataDirectory;
import ch.bailu.aat.preferences.SolidDirectory;
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
            list.add(labelFactory(ActivitySwitcher.list[i]));
        }
        return list;
    }


    private void createDispatcher() {
        addTarget(gpsState, INFO_ID_LOCATION);
        addTarget(trackerState, INFO_ID_TRACKER);

        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
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


    private ActivityLabel labelFactory(ActivitySwitcher s) {
        if (s.activityClass == TrackListActivity.class) {
            return new PresetDirectoryLabel(s);
        } else if (s.activityClass == OverlayListActivity.class) {
            return new DirectoryLabel(s, AppDirectory.DIR_OVERLAY);
        } else if (s.activityClass == ImportListActivity.class) {
            return new DirectoryLabel(s, AppDirectory.DIR_IMPORT);
        }

        return new ActivityLabel(s);

    }

    private class ActivityLabel extends AbsLabelTextView {
        public ActivityLabel(final ActivitySwitcher s) {
            super(MainActivity.this, getString(s.activityLabel));

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.start(MainActivity.this);
                }
            });
        }

    }


    private class PresetDirectoryLabel extends ActivityLabel implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final SolidDirectory sdirectory;
        private final SolidPreset spreset;


        public PresetDirectoryLabel(ActivitySwitcher s) {
            super(s);
            sdirectory = new SolidDataDirectory(getContext());
            spreset = new SolidPreset(getContext());

            setText();
        }

        public void setText() {
            setText(new SolidPreset(getContext()).getDirectoryName());
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();

            spreset.register(this);
        }


        @Override
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();

            spreset.unregister(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (spreset.hasKey(key) || sdirectory.hasKey(key)) {
                setText();
            }
        }
    }


    private class DirectoryLabel extends ActivityLabel implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final SolidDirectory sdirectory;
        private final String directory;


        public DirectoryLabel(ActivitySwitcher s, String d) {
            super(s);
            sdirectory = new SolidDataDirectory(getContext());
            directory = d;
        }

        public void setText() {
            setText(AppDirectory.getDataDirectory(getContext(), directory).getAbsolutePath());
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            setText();
            sdirectory.register(this);
        }


        @Override
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            sdirectory.unregister(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (sdirectory.hasKey(key)) {
                setText();
            }
        }
    }
}
