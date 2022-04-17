package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.presets.SolidBacklight;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat_lib.preferences.location.SolidDistanceFilter;
import ch.bailu.aat_lib.preferences.presets.SolidAccuracyFilter;
import ch.bailu.aat_lib.preferences.presets.SolidMET;
import ch.bailu.aat_lib.preferences.presets.SolidMissingTrigger;
import ch.bailu.aat_lib.preferences.presets.SolidTrackerAutopause;

public class PresetPreferencesView extends VerticalScrollView {
    public PresetPreferencesView(Context context, int index, UiTheme theme) {
        super(context);

        add(new TitleView(
                context,
                context.getString(R.string.p_preset) + " " + (index + 1),
                theme)
        );
        StorageInterface storage = new Storage(context);
        add(new SolidStringView(context,new SolidMET(storage, index), theme));
        add(new SolidIndexListView(context,new SolidPostprocessedAutopause(storage, index), theme));
        add(new SolidIndexListView(context,new SolidTrackerAutopause(storage, index), theme));
        add(new SolidIndexListView(context,new SolidDistanceFilter(storage, index), theme));
        add(new SolidIndexListView(context,new SolidAccuracyFilter(storage, index), theme));
        add(new SolidIndexListView(context,new SolidMissingTrigger(storage, index), theme));
        add(new SolidIndexListView(context,new SolidBacklight(context, index), theme));
    }

}
