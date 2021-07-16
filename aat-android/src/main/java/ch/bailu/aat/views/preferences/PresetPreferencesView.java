package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat.preferences.presets.SolidAccuracyFilter;
import ch.bailu.aat.preferences.presets.SolidBacklight;
import ch.bailu.aat.preferences.presets.SolidDistanceFilter;
import ch.bailu.aat.preferences.presets.SolidMET;
import ch.bailu.aat.preferences.presets.SolidMissingTrigger;
import ch.bailu.aat.preferences.presets.SolidTrackerAutopause;
import ch.bailu.aat.util.ui.UiTheme;

public class PresetPreferencesView extends VerticalScrollView {
    public PresetPreferencesView(Context context, int i, UiTheme theme) {
        super(context);

        add(new TitleView(
                context,
                context.getString(R.string.p_preset) + " " + (i + 1),
                theme)
        );
        add(new SolidStringView(new SolidMET(context, i), theme));
        add(new SolidIndexListView(new SolidPostprocessedAutopause(context, i), theme));
        add(new SolidIndexListView(new SolidTrackerAutopause(context, i), theme));
        add(new SolidIndexListView(new SolidDistanceFilter(context, i), theme));
        add(new SolidIndexListView(new SolidAccuracyFilter(context, i), theme));
        add(new SolidIndexListView(new SolidMissingTrigger(context, i), theme));
        add(new SolidIndexListView(new SolidBacklight(context, i), theme));
    }

}
