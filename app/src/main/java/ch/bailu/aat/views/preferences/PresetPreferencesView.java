package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidAccuracyFilter;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidDistanceFilter;
import ch.bailu.aat.preferences.SolidMET;
import ch.bailu.aat.preferences.SolidMissingTrigger;
import ch.bailu.aat.preferences.SolidTrackerAutopause;

public class PresetPreferencesView extends VerticalScrollView {
    public PresetPreferencesView(Context context, int i) {
        super(context);

        add(new TitleView(context, context.getString(R.string.p_preset) + " " + (i + 1)));

        add(new SolidStringView(new SolidMET(context, i)));
        add(new SolidIndexListView(new SolidTrackerAutopause(context, i)));
        add(new SolidIndexListView(new SolidDistanceFilter(context, i)));
        add(new SolidIndexListView(new SolidAccuracyFilter(context, i)));
        add(new SolidIndexListView(new SolidMissingTrigger(context, i)));
    }

}
