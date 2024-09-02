package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.app.ActivitySwitcher
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault
import ch.bailu.aat.preferences.system.SolidExternalDirectory
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat.views.msg.permission.DataDirectoryPermissionInfoView
import ch.bailu.aat.views.msg.permission.LocationPermissionInfoView
import ch.bailu.aat.views.preferences.SolidIndexListView
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.SensorSource
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc_android.FocAndroidFactory


class MainActivity : ActivityContext() {
    private val theme = AppTheme.intro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViews()
        createDispatcher()
    }

    override fun onResumeWithService() {
        super.onResumeWithService()
        appContext.broadcaster.broadcast( AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS)
    }

    private fun createViews() {
        val contentView = ContentView(this, theme)
        contentView.add(createButtonBar())
        contentView.add(permissionInfo())
        contentView.addW(createActionList())
        setContentView(contentView)
    }

    private fun permissionInfo(): View {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(LocationPermissionInfoView(this@MainActivity, theme))
            addView(DataDirectoryPermissionInfoView(this@MainActivity, appContext.dataDirectory, theme))
        }
    }

    private fun createActionList(): View {
        val list = VerticalScrollView(this)
        list.add(SolidIndexListView(this, SolidPreset(appContext.storage), theme))
        val accessibleCount = ActivitySwitcher(this).size()
        for (i in 0 until accessibleCount) {
            list.add(labelFactory(ActivitySwitcher(this)[i]))
        }
        return list
    }

    private fun createDispatcher() {
        dispatcher.addSource(TrackerSource(serviceContext, appContext.broadcaster))
        dispatcher.addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.SENSORS))
    }

    private fun createButtonBar(): LinearLayout {
        val bar = MainControlBar(this)
        bar.addSensorState(this)
        if (AppLayout.haveExtraSpaceGps(this)) {
            bar.addSpace()
        }
        bar.addGpsState(this)
        bar.addTrackerState(this)
        return bar
    }

    private fun labelFactory(s: ActivitySwitcher.Entry): ActivityLabel {
        return when (s.activityClass) {
            TrackListActivity::class.java -> PresetDirectoryLabel(s)
            OverlayListActivity::class.java -> InternalDirectoryLabel(s, AppDirectory.DIR_OVERLAY)
            ExternalListActivity::class.java -> ExternalDirectoryLabel(s)
            else -> ActivityLabel(s)
        }
    }

    private open inner class ActivityLabel(theme: UiTheme, s: ActivitySwitcher.Entry) :
        LabelTextView(this@MainActivity, s.activityLabel, theme) {
        constructor(s: ActivitySwitcher.Entry) : this(theme, s)

        init {
            setOnClickListener { s.start(this@MainActivity) }
            theme.button(this)
            setText(s.activitySubLabel)
        }
    }

    private inner class ExternalDirectoryLabel(s: ActivitySwitcher.Entry) : ActivityLabel(s), OnPreferencesChanged {
        private val sdirectory: SolidExternalDirectory = SolidExternalDirectory(this@MainActivity)

        init {
            setText()
        }

        fun setText() {
            visibility = if (sdirectory.getValueAsFile().canRead()) {
                VISIBLE
            } else {
                GONE
            }
            setText(sdirectory.toString())
        }

        public override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            sdirectory.register(this)
        }

        public override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            sdirectory.unregister(this)
        }

        override fun onPreferencesChanged(
             storage: StorageInterface,
             key: String
        ) {
            if (sdirectory.hasKey(key)) {
                setText()
            }
        }
    }

    private inner class PresetDirectoryLabel(s: ActivitySwitcher.Entry) : ActivityLabel(s), OnPreferencesChanged {
        private val sdirectory = SolidDataDirectory(AndroidSolidDataDirectoryDefault(context), FocAndroidFactory(context))
        private val spreset: SolidPreset = SolidPreset(appContext.storage)

        init {
            setText()
        }

        fun setText() {
            setText(SolidPreset(appContext.storage).getDirectory(sdirectory).pathName)
        }

        public override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            spreset.register(this)
        }

        public override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            spreset.unregister(this)
        }

        override fun onPreferencesChanged(storage: StorageInterface, key: String
        ) {
            if (spreset.hasKey(key) || sdirectory.hasKey(key)) {
                setText()
            }
        }
    }

    private inner class InternalDirectoryLabel(s: ActivitySwitcher.Entry, private val directory: String) :
        ActivityLabel(s), OnPreferencesChanged {
        private val sdirectory = SolidDataDirectory(AndroidSolidDataDirectoryDefault(context), FocAndroidFactory(context))

        fun setText() {
            setText(AppDirectory.getDataDirectory(sdirectory, directory).pathName)
        }

        public override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            setText()
            sdirectory.register(this)
        }

        public override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            sdirectory.unregister(this)
        }

        override fun onPreferencesChanged(storage: StorageInterface, key: String) {
            if (sdirectory.hasKey(key)) {
                setText()
            }
        }
    }
}
