package ch.bailu.aat.views.preferences;

import android.content.Context;

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
    public GeneralPreferencesView(Context context) {
        super(context);

        add(new TitleView(context, R.string.p_general));
        add(new SolidIndexListView(context,new SolidUnit(context)));
        add(new SolidIntegerView(context,new SolidWeight(context)));


        final SolidPreset spreset= new SolidPreset(context);
        for (int i=0; i<spreset.length(); i++) {
            add(new TitleView(context, context.getString(R.string.p_preset) + " " + (i+1)));

            add(new SolidIndexListView(context,new SolidMET(context,i)));
            add(new SolidIndexListView(context,new SolidAutopause(context,i)));
            add(new SolidIndexListView(context,new SolidDistanceFilter(context,i)));
            add(new SolidIndexListView(context,new SolidAccelerationFilter(context,i)));
            add(new SolidIndexListView(context,new SolidAccuracyFilter(context,i)));
            add(new SolidIndexListView(context,new SolidMissingTrigger(context,i)));
        }

        add(new TitleView(context, R.string.p_system));
        add(new SolidIndexListView(context,new SolidLocationProvider(context)));
        add(new SolidIndexListView(context,new SolidDataDirectory(context)));
    }
}
