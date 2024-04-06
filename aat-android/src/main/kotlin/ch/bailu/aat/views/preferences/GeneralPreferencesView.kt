package ch.bailu.aat.views.preferences

import ch.bailu.aat.R
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.location.AndroidSolidLocationProvider
import ch.bailu.aat.preferences.location.SolidGpsTimeFix
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault
import ch.bailu.aat.preferences.system.SolidExternalDirectory
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.list.SensorListView
import ch.bailu.aat.views.preferences.dialog.SolidTextInputDialog
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitudeValue
import ch.bailu.aat_lib.preferences.location.SolidPressureAtSeaLevel
import ch.bailu.aat_lib.preferences.location.SolidProvideAltitude
import ch.bailu.aat_lib.preferences.system.SolidCacheSize
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages
import ch.bailu.foc_android.FocAndroidFactory

class GeneralPreferencesView(acontext: ActivityContext, theme: UiTheme) :
    VerticalScrollView(acontext) {
    init {
        val storage: StorageInterface = Storage(acontext)
        add(TitleView(acontext, R.string.p_general, theme))
        add(SolidIndexListView(acontext, SolidUnit(storage), theme))
        add(
            SolidTextInputView(
                acontext, SolidWeight(storage),
                SolidTextInputDialog.INTEGER, theme
            )
        )
        add(SolidIndexListView(acontext, SolidPresetCount(storage), theme))
        add(SolidIndexListView(acontext, SolidStatusMessages(storage), theme))
        add(TitleView(acontext, "GPS", theme))
        add(SolidIndexListView(acontext, AndroidSolidLocationProvider(acontext), theme))
        add(SolidCheckBox(acontext, SolidGpsTimeFix(storage), theme))
        add(SolidCheckBox(acontext, SolidAdjustGpsAltitude(storage), theme))
        add(
            SolidTextInputView(
                acontext,
                SolidAdjustGpsAltitudeValue(storage, SolidUnit(storage).index),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme
            )
        )
        add(
            SolidTextInputView(
                acontext, SolidPressureAtSeaLevel(storage),
                SolidTextInputDialog.FLOAT,
                theme
            )
        )
        add(
            SolidTextInputView(
                acontext, SolidProvideAltitude(storage, SolidUnit.SI),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme
            )
        )
        add(
            SolidTextInputView(
                acontext, SolidProvideAltitude(storage, SolidUnit.IMPERIAL),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme
            )
        )
        add(TitleView(acontext, R.string.sensors, theme))
        val scan = ScanBluetoothView(
            acontext.serviceContext, theme
        )
        val updateConnection = ConnectToSensorsView(
            acontext.serviceContext,
            theme
        )
        val sensors = SensorListView(
            acontext.serviceContext,
            theme
        )
        add(scan)
        add(updateConnection)
        add(sensors)
        acontext.addTarget(scan, InfoID.SENSORS)
        acontext.addTarget(updateConnection, InfoID.SENSORS)
        acontext.addTarget(sensors, InfoID.SENSORS)
        add(TitleView(acontext, R.string.files, theme))
        add(SolidDirectoryViewSAF(acontext, SolidDataDirectory(AndroidSolidDataDirectoryDefault(context), FocAndroidFactory(context)), theme))
        add(SolidDirectoryViewSAF(acontext, SolidExternalDirectory(acontext), theme))
        add(SolidIndexListView(acontext, SolidCacheSize(storage), theme))
    }
}
