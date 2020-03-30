package ch.bailu.aat.views.preferences;

import android.os.Build;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.preferences.general.SolidPresetCount;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.preferences.general.SolidWeight;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitudeValue;
import ch.bailu.aat.preferences.location.SolidGpsTimeFix;
import ch.bailu.aat.preferences.location.SolidLocationProvider;
import ch.bailu.aat.preferences.location.SolidPressureAtSeaLevel;
import ch.bailu.aat.preferences.location.SolidProvideAltitude;
import ch.bailu.aat.preferences.system.SolidCacheSize;
import ch.bailu.aat.preferences.system.SolidDataDirectory;
import ch.bailu.aat.preferences.system.SolidExternalDirectory;
import ch.bailu.aat.preferences.system.SolidStatusMessages;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.SensorListView;


public class GeneralPreferencesView extends VerticalScrollView {

    public GeneralPreferencesView(ActivityContext acontext, UiTheme theme) {
        super(acontext);



        add(new TitleView(acontext, R.string.p_general, theme));
        add(new SolidIndexListView(new SolidUnit(acontext), theme));
        add(new SolidTextInputView(new SolidWeight(acontext),
                SolidTextInputDialog.INTEGER, theme));

        add(new SolidIndexListView(new SolidPresetCount(acontext), theme));
        add(new SolidIndexListView( new SolidStatusMessages(acontext), theme));

        add(new TitleView(acontext, "GPS", theme));
        add(new SolidIndexListView(new SolidLocationProvider(acontext), theme));
        add(new SolidCheckBox(new SolidGpsTimeFix(acontext), theme));
        add(new SolidCheckBox(new SolidAdjustGpsAltitude(acontext), theme));
        add(new SolidTextInputView(
                new SolidAdjustGpsAltitudeValue(acontext, new SolidUnit(acontext).getIndex()),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme));

        add(new SolidTextInputView(new SolidPressureAtSeaLevel(acontext),
                SolidTextInputDialog.FLOAT,
                theme));

        add(new SolidTextInputView(new SolidProvideAltitude(acontext, SolidUnit.SI),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme));
        add(new SolidTextInputView(new SolidProvideAltitude(acontext, SolidUnit.IMPERIAL),
                SolidTextInputDialog.INTEGER_SIGNED,
                theme));

        if (Build.VERSION.SDK_INT >= 18) {
            add(new TitleView(acontext, ToDo.translate("Sensors"), theme));
            ScannBluetoothView scann = new ScannBluetoothView(
                    acontext.getServiceContext(), theme
            );
            ConnectToSensorsView updateConnection = new ConnectToSensorsView(
                    acontext.getServiceContext(),
                    theme
            );
            SensorListView sensors = new SensorListView(acontext.getServiceContext(), theme);

            add(scann);
            add(updateConnection);
            add(sensors);
            acontext.addTarget(scann, InfoID.SENSORS);
            acontext.addTarget(updateConnection, InfoID.SENSORS);
            acontext.addTarget(sensors, InfoID.SENSORS);
        }


        add(new TitleView(acontext, ToDo.translate("Files"), theme));
        add(new SolidDirectoryViewSAF(acontext, new SolidDataDirectory(acontext), theme));
        add(new SolidDirectoryViewSAF(acontext, new SolidExternalDirectory(acontext), theme));

        add(new SolidIndexListView( new SolidCacheSize(acontext), theme));
    }

}
