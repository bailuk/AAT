package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidCacheSize;
import ch.bailu.aat.preferences.SolidGpsTimeFix;
import ch.bailu.aat.preferences.SolidPostprocessedAutopause;
import ch.bailu.aat.preferences.SolidDataDirectory;
import ch.bailu.aat.preferences.SolidExternalDirectory;
import ch.bailu.aat.preferences.SolidLocationProvider;
import ch.bailu.aat.preferences.SolidUnit;
import ch.bailu.aat.preferences.SolidWeight;


public class GeneralPreferencesView extends VerticalScrollView {

    public GeneralPreferencesView(Activity context) {
        super(context);

        add(new TitleView(context, R.string.p_general));
        add(new SolidIndexListView(new SolidUnit(context)));
        add(new SolidIntegerView(new SolidWeight(context)));
        add(new SolidIndexListView(new SolidPostprocessedAutopause(context)));

        add(new TitleView(context, R.string.p_system));
        add(new SolidIndexListView(new SolidLocationProvider(context)));
        add(new SolidCheckBox(new SolidGpsTimeFix(context)));
        add(new SolidDirectoryViewSAF(context, new SolidDataDirectory(context)));
        add(new SolidDirectoryViewSAF(context, new SolidExternalDirectory(context)));
        add(new SolidIndexListView( new SolidCacheSize(context)));
    }


}
