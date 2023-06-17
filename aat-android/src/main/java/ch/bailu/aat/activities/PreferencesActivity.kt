package ch.bailu.aat.activities

import android.content.Intent
import android.os.Bundle
import ch.bailu.aat.R
import ch.bailu.aat.dispatcher.SensorSource
import ch.bailu.aat.preferences.SolidSAF
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.preferences.GeneralPreferencesView
import ch.bailu.aat.views.preferences.MapPreferencesView
import ch.bailu.aat.views.preferences.PresetPreferencesView
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import javax.annotation.Nonnull

class PreferencesActivity : ActivityContext(), OnPreferencesChanged {
    companion object {
        @JvmField
        val SOLID_KEY: String = PreferencesActivity::class.java.simpleName
    }

    private val theme = AppTheme.preferences
    private var mapTilePreferences: MapPreferencesView? = null
    private var multiView: MultiView? = null
    private var spresetCount: SolidPresetCount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spresetCount = SolidPresetCount(Storage(this))
        spresetCount?.register(this)
        createViews()
        addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.SENSORS))
    }

    private fun createViews() {
        multiView = createMultiView(theme).apply {
            val contentView = ContentView(this@PreferencesActivity, theme)
            contentView.addMvIndicator(this)
            contentView.add(MainControlBar(this@PreferencesActivity, this))
            contentView.add(errorView)
            contentView.add(this)
            setContentView(contentView)
        }
    }

    private fun createMultiView(theme: UiTheme): MultiView {
        val multiView = MultiView(this, SOLID_KEY)
        mapTilePreferences = MapPreferencesView(this, serviceContext, theme)
        multiView.add(
            GeneralPreferencesView(this, theme),
            getString(R.string.p_general) + "/" + getString(R.string.p_system)
        )
        multiView.add(
            mapTilePreferences,
            getString(R.string.p_tiles)
        )
        addPresetPreferences(multiView, theme)
        return multiView
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        SolidSAF.onActivityResult(this, requestCode, resultCode, data)
    }

    override fun onResumeWithService() {
        super.onResumeWithService()
        mapTilePreferences?.updateText()
    }

    override fun onDestroy() {
        spresetCount?.unregister(this)
        super.onDestroy()
    }

    override fun onPreferencesChanged(@Nonnull s: StorageInterface, @Nonnull key: String) {
        spresetCount?.apply {
            if (this.hasKey(key)) {
                multiView?.apply { addPresetPreferences(this, theme) }
            }
        }
    }

    private fun addPresetPreferences(multiView: MultiView, theme: UiTheme) {
        while (multiView.pageCount() > 2) multiView.remove(multiView.pageCount() - 1)

        for (i in 0 until getPresetCount()) {
            multiView.add(
                PresetPreferencesView(this, i, theme),
                getString(R.string.p_preset) + " " + (i + 1)
            )
        }
    }

    private fun getPresetCount(): Int {
        var presentCount = 0
        spresetCount?.apply {
            presentCount = value
        }
        return presentCount
    }
}
