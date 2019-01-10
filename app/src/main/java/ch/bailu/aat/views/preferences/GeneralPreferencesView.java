package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat.preferences.general.SolidPresetCount;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.preferences.general.SolidWeight;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitudeValue;
import ch.bailu.aat.preferences.location.SolidGpsTimeFix;
import ch.bailu.aat.preferences.location.SolidLocationProvider;
import ch.bailu.aat.preferences.location.SolidPressureAtSeaLevel;
import ch.bailu.aat.preferences.location.SolidProvideAltitude;
import ch.bailu.aat.preferences.location.SolidAltitudeFromBarometer;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat.preferences.system.SolidCacheSize;
import ch.bailu.aat.preferences.system.SolidDataDirectory;
import ch.bailu.aat.preferences.system.SolidExternalDirectory;
import ch.bailu.aat.preferences.system.SolidStatusMessages;
import ch.bailu.aat.util.ToDo;


public class GeneralPreferencesView extends VerticalScrollView {

    public GeneralPreferencesView(Activity context) {
        super(context);

        add(new TitleView(context, R.string.p_general));
        add(new SolidIndexListView(new SolidUnit(context)));
        add(new SolidTextInputView(new SolidWeight(context),
                SolidTextInputDialog.INTEGER));

        add(new SolidIndexListView(new SolidPostprocessedAutopause(context)));
        add(new SolidIndexListView(new SolidPresetCount(context)));
        add(new SolidIndexListView( new SolidStatusMessages(context)));

        add(new TitleView(context, "GPS"));
        add(new SolidIndexListView(new SolidLocationProvider(context)));
        add(new SolidCheckBox(new SolidGpsTimeFix(context)));
        add(new SolidCheckBox(new SolidAdjustGpsAltitude(context)));
        add(new SolidTextInputView(new SolidAdjustGpsAltitudeValue(context, new SolidUnit(context).getIndex()),
                SolidTextInputDialog.INTEGER_SIGNED));

        add(new SolidCheckBox(new SolidAltitudeFromBarometer(context)));
        add(new SolidTextInputView(new SolidPressureAtSeaLevel(context),
                SolidTextInputDialog.FLOAT));

        add(new SolidTextInputView(new SolidProvideAltitude(context, SolidUnit.SI),
                SolidTextInputDialog.INTEGER_SIGNED));
        add(new SolidTextInputView(new SolidProvideAltitude(context, SolidUnit.IMPERIAL),
                SolidTextInputDialog.INTEGER_SIGNED));

        add(new TitleView(context, ToDo.translate("Files")));
        add(new SolidDirectoryViewSAF(context, new SolidDataDirectory(context)));
        add(new SolidDirectoryViewSAF(context, new SolidExternalDirectory(context)));

        add(new SolidIndexListView( new SolidCacheSize(context)));
    }
}
