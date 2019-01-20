package ch.bailu.aat.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.preferences.presets.SolidPreset;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.preferences.system.SolidDataDirectory;
import ch.bailu.aat.preferences.system.SolidExternalDirectory;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.AbsLabelTextView;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.preferences.SolidIndexListView;
import ch.bailu.aat.views.preferences.VerticalScrollView;


public class MainActivity extends ActivityContext {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViews();
        createDispatcher();
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();

        AppBroadcaster.broadcast(this, AppBroadcaster.BLE_DEVICE_SCANNED);
    }


    private void createViews() {

        ContentView contentView = new ContentView(this);

        contentView.add(createButtonBar());
        contentView.addW(createActionList());

        contentView.add(createExtraButton());

        setContentView(contentView);
    }


    private View createExtraButton() {
        LinearLayout layout = new LinearLayout(this);
        AppTheme.alt.background(layout);
        layout.setOrientation(LinearLayout.VERTICAL);

        if (Build.VERSION.SDK_INT >= 18) {
            layout.addView(new BleLabel());
        }

        layout.addView(labelFactory(ActivitySwitcher.getAbout(this)));
        //layout.addView(new DocumentationLabel(ActivitySwitcher.getAbout(this)));
        layout.setBackgroundColor(AppTheme.getAltBackgroundColor());

        return layout;
    }

    private View createActionList() {

        final VerticalScrollView list = new VerticalScrollView(this);
        list.add(new SolidIndexListView(new SolidPreset(this)));

        final int accessibleCount = new ActivitySwitcher(this).size();
        for (int i = 0; i < accessibleCount; i++) {
            list.add(labelFactory(new ActivitySwitcher(this).get(i)));
        }


        return list;
    }


    private void createDispatcher() {
        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
    }



    private LinearLayout createButtonBar() {
        final MainControlBar bar = new MainControlBar(this);

        bar.addSpace();
        if (AppLayout.haveExtraSpaceGps(this)) {
            bar.addSpace();
        }

        bar.addGpsState(this);
        bar.addTrackerState(this);

        return bar;

    }


    private ActivityLabel labelFactory(ActivitySwitcher.Entry s) {
        if (s.activityClass == TrackListActivity.class) {
            return new PresetDirectoryLabel(s);
        } else if (s.activityClass == OverlayListActivity.class) {
            return new InternalDirectoryLabel(s, AppDirectory.DIR_OVERLAY);
        } else if (s.activityClass == ExternalListActivity.class) {
            return new ExternalDirectoryLabel(s);
        } else if (s.activityClass == AboutActivity.class) {
            ActivityLabel label = new ActivityLabel(AppTheme.alt, s);
            label.setText(getString(R.string.intro_about) + " / " + getString(R.string.intro_readme));
            return label;
        }

        return new ActivityLabel(s);

    }

    private class ActivityLabel extends AbsLabelTextView {
        public ActivityLabel(final ActivitySwitcher.Entry s) {
            this(AppTheme.main, s);
        }

        public ActivityLabel(UiTheme theme, final ActivitySwitcher.Entry s) {
            super( MainActivity.this, theme, s.activityLabel);

            setOnClickListener(v -> s.start(MainActivity.this));

            setText(s.activitySubLabel);
        }

    }


    private class ExternalDirectoryLabel extends ActivityLabel implements SharedPreferences.OnSharedPreferenceChangeListener {
        private final SolidExternalDirectory sdirectory;

        public ExternalDirectoryLabel(final ActivitySwitcher.Entry s) {
            super(s);
            sdirectory = new SolidExternalDirectory(MainActivity.this);
            setText();
        }

        public void setText() {

            if (sdirectory.getValueAsFile().canRead()) {
                setVisibility(VISIBLE);
            } else {
                setVisibility(GONE);
            }

            setText(sdirectory.toString());
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();

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



    private class PresetDirectoryLabel extends ActivityLabel implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final SolidFile sdirectory;
        private final SolidPreset spreset;


        public PresetDirectoryLabel(ActivitySwitcher.Entry s) {
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


    private class InternalDirectoryLabel extends ActivityLabel implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final SolidFile sdirectory;
        private final String directory;


        public InternalDirectoryLabel(ActivitySwitcher.Entry s, String d) {
            super(s);
            sdirectory = new SolidDataDirectory(getContext());
            directory = d;
        }

        public void setText() {
            setText(AppDirectory.getDataDirectory(getContext(), directory).getPathName());
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

    private class DocumentationLabel extends ActivityLabel {

        public DocumentationLabel(ActivitySwitcher.Entry s) {
            super(s);
            setTextColor(AppTheme.getHighlightColor3());

            setText(getString(R.string.intro_about) + " / " + getString(R.string.intro_readme));
        }


    }


    private class BleLabel extends AbsLabelTextView implements View.OnClickListener {
        public BleLabel() {
            super(MainActivity.this, ToDo.translate("Bluetooth LE sensors"));
            setText();
            setOnClickListener(this);
        }


        @Override
        public void onDetachedFromWindow() {
            getContext().unregisterReceiver(onBleDeviceScanned);
            super.onDetachedFromWindow();
        }


        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            AppBroadcaster.register(getContext(), onBleDeviceScanned, AppBroadcaster.BLE_DEVICE_SCANNED);
        }


        private final BroadcastReceiver onBleDeviceScanned = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setText();
            }
        };

        private void setText() {
            new InsideContext(getServiceContext()) {
                @Override
                public void run() {
                    setText(getServiceContext().getBleService().toString());
                }
            };
        }


        @Override
        public void onClick(View v) {
            new InsideContext(getServiceContext()) {
                @Override
                public void run() {
                    getServiceContext().getBleService().scan();
                }
            };
        }
    }

}
