package ch.bailu.aat.views.preferences;

import android.os.Build;
import android.view.View;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat.preferences.general.SolidPresetCount;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.preferences.general.SolidWeight;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitudeValue;
import ch.bailu.aat.preferences.location.SolidAltitudeFromBarometer;
import ch.bailu.aat.preferences.location.SolidGpsTimeFix;
import ch.bailu.aat.preferences.location.SolidLocationProvider;
import ch.bailu.aat.preferences.location.SolidPressureAtSeaLevel;
import ch.bailu.aat.preferences.location.SolidProvideAltitude;
import ch.bailu.aat.preferences.system.SolidCacheSize;
import ch.bailu.aat.preferences.system.SolidDataDirectory;
import ch.bailu.aat.preferences.system.SolidExternalDirectory;
import ch.bailu.aat.preferences.system.SolidStatusMessages;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.views.AbsLabelTextView;
import ch.bailu.aat.views.SensorListView;


public class GeneralPreferencesView extends VerticalScrollView {

    public GeneralPreferencesView(ActivityContext acontext) {
        super(acontext);

        add(new TitleView(acontext, R.string.p_general));
        add(new SolidIndexListView(new SolidUnit(acontext)));
        add(new SolidTextInputView(new SolidWeight(acontext),
                SolidTextInputDialog.INTEGER));

        add(new SolidIndexListView(new SolidPostprocessedAutopause(acontext)));
        add(new SolidIndexListView(new SolidPresetCount(acontext)));
        add(new SolidIndexListView( new SolidStatusMessages(acontext)));

        add(new TitleView(acontext, "GPS"));
        add(new SolidIndexListView(new SolidLocationProvider(acontext)));
        add(new SolidCheckBox(new SolidGpsTimeFix(acontext)));
        add(new SolidCheckBox(new SolidAdjustGpsAltitude(acontext)));
        add(new SolidTextInputView(new SolidAdjustGpsAltitudeValue(acontext, new SolidUnit(acontext).getIndex()),
                SolidTextInputDialog.INTEGER_SIGNED));

        add(new SolidCheckBox(new SolidAltitudeFromBarometer(acontext)));
        add(new SolidTextInputView(new SolidPressureAtSeaLevel(acontext),
                SolidTextInputDialog.FLOAT));

        add(new SolidTextInputView(new SolidProvideAltitude(acontext, SolidUnit.SI),
                SolidTextInputDialog.INTEGER_SIGNED));
        add(new SolidTextInputView(new SolidProvideAltitude(acontext, SolidUnit.IMPERIAL),
                SolidTextInputDialog.INTEGER_SIGNED));

        if (Build.VERSION.SDK_INT >= 18) {
            add(new TitleView(acontext, ToDo.translate("Sensors")));
            BleLabel label = new BleLabel(acontext.getServiceContext());
            SensorListView sensors = new SensorListView(acontext.getServiceContext());

            add(label);
            add(sensors);
            acontext.addTarget(label, InfoID.SENSORS);
            acontext.addTarget(sensors, InfoID.SENSORS);
        }


        add(new TitleView(acontext, ToDo.translate("Files")));
        add(new SolidDirectoryViewSAF(acontext, new SolidDataDirectory(acontext)));
        add(new SolidDirectoryViewSAF(acontext, new SolidExternalDirectory(acontext)));

        add(new SolidIndexListView( new SolidCacheSize(acontext)));




    }

    private class BleLabel extends AbsLabelTextView implements View.OnClickListener, OnContentUpdatedInterface {
        private final ServiceContext scontext;
        public BleLabel(ServiceContext s) {
            super(s.getContext(), ToDo.translate("Scann for BluetoothLE sensors"));
            scontext = s;
            setText();
            setOnClickListener(this);
        }


        private void setText() {
            new InsideContext(scontext) {
                @Override
                public void run() {
                    setText(scontext.getSensorService().toString());
                }
            };
        }


        @Override
        public void onClick(View v) {
            new InsideContext(scontext) {
                @Override
                public void run() {
                    scontext.getSensorService().scann();
                }
            };
        }

        @Override
        public void onContentUpdated(int iid, GpxInformation info) {
            if (iid == InfoID.SENSORS) {
                setText();
            }

        }
    }
}
