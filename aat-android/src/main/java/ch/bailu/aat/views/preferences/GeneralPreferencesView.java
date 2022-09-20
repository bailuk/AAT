package ch.bailu.aat.views.preferences;

import android.os.Build;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.general.SolidWeight;
import ch.bailu.aat.preferences.location.AndroidSolidLocationProvider;
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitudeValue;
import ch.bailu.aat.preferences.location.SolidGpsTimeFix;
import ch.bailu.aat_lib.preferences.location.SolidPressureAtSeaLevel;
import ch.bailu.aat_lib.preferences.location.SolidProvideAltitude;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat_lib.preferences.system.SolidCacheSize;
import ch.bailu.aat.preferences.system.SolidExternalDirectory;
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.SensorListView;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidPresetCount;
import ch.bailu.aat_lib.preferences.general.SolidUnit;


public class GeneralPreferencesView extends VerticalScrollView {

    public GeneralPreferencesView(ActivityContext acontext, UiTheme theme) {
        super(acontext);

        StorageInterface storage = new Storage(acontext);

        add(new TitleView(acontext, R.string.p_general, theme));
        add(new SolidIndexListView(acontext,new SolidUnit(storage), theme));
        add(new SolidTextInputView(acontext,new SolidWeight(storage),
                SolidTextInputDialog.INTEGER, theme));

        add(new SolidIndexListView(acontext,new SolidPresetCount(storage), theme));
        add(new SolidIndexListView(acontext,new SolidStatusMessages(storage), theme));

        add(new TitleView(acontext, "GPS", theme));
        add(new SolidIndexListView(acontext,new AndroidSolidLocationProvider(acontext), theme));
        add(new SolidCheckBox(acontext,new SolidGpsTimeFix(storage), theme));
        add(new SolidCheckBox(acontext,new SolidAdjustGpsAltitude(storage), theme));
        add(new SolidTextInputView(acontext,
                new SolidAdjustGpsAltitudeValue(storage, new SolidUnit(storage).getIndex()),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme));

        add(new SolidTextInputView(acontext,new SolidPressureAtSeaLevel(storage),
                SolidTextInputDialog.FLOAT,
                theme));

        add(new SolidTextInputView(acontext,new SolidProvideAltitude(storage, SolidUnit.SI),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme));
        add(new SolidTextInputView(acontext,new SolidProvideAltitude(storage, SolidUnit.IMPERIAL),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme));

        if (Build.VERSION.SDK_INT >= 18) {
            add(new TitleView(acontext, R.string.sensors, theme));
            ScanBluetoothView scan = new ScanBluetoothView(
                    acontext.getServiceContext(), theme
            );
            ConnectToSensorsView updateConnection = new ConnectToSensorsView(
                    acontext.getServiceContext(),
                    theme
            );
            SensorListView sensors = new SensorListView(acontext.getServiceContext(), theme);

            add(scan);
            add(updateConnection);
            add(sensors);
            acontext.addTarget(scan, InfoID.SENSORS);
            acontext.addTarget(updateConnection, InfoID.SENSORS);
            acontext.addTarget(sensors, InfoID.SENSORS);
        }

        add(new TitleView(acontext, R.string.files, theme));
        add(new SolidDirectoryViewSAF(acontext, new AndroidSolidDataDirectory(acontext), theme));
        add(new SolidDirectoryViewSAF(acontext, new SolidExternalDirectory(acontext), theme));

        add(new SolidIndexListView(acontext, new SolidCacheSize(storage), theme));
    }
}
