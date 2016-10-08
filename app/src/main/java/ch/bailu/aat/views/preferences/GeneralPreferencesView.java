package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidAccelerationFilter;
import ch.bailu.aat.preferences.SolidAccuracyFilter;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidDataDirectory;
import ch.bailu.aat.preferences.SolidDistanceFilter;
import ch.bailu.aat.preferences.SolidLocationProvider;
import ch.bailu.aat.preferences.SolidMET;
import ch.bailu.aat.preferences.SolidMissingTrigger;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidUnit;
import ch.bailu.aat.preferences.SolidWeight;


public class GeneralPreferencesView extends VerticalScrollView {

    public GeneralPreferencesView(Activity context) {
        super(context);

        add(new TitleView(context, R.string.p_general));
        add(new SolidIndexListView(new SolidUnit(context)));
        add(new SolidIntegerView(new SolidWeight(context)));

        add(new TitleView(context, R.string.p_system));
        add(new SolidIndexListView(new SolidLocationProvider(context)));
        add(new SolidExtendetDirectoryView(context, new SolidDataDirectory(context)));
    }


}
