package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.theme.AppTheme;
import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat.views.description.DescriptionLabelTextView;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.foc_android.FocAndroidFactory;


public class VerticalScrollView extends ScrollView {
    private final LinearLayout layout;

    public VerticalScrollView(Context context) {
        super(context);

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        addView(layout);
    }



    public void add(View view) {
        AppTheme.padding(view);
        layout.addView(view);
    }

    public void add(DispatcherInterface di, ContentDescription d, UiTheme theme, int... iid) {
        final DescriptionLabelTextView v = new DescriptionLabelTextView(getContext(), d, theme);

        add(v);

        for (int i : iid) di.addTarget(v, i);

    }


    public void addAllContent(DispatcherInterface di,
                              ContentDescription[] descriptions,
                              UiTheme theme,
                              int... iid) {
        for (ContentDescription description : descriptions) {
            add(di, description, theme, iid);
        }
    }


    public void addAllFilterViews(MapContext mc, UiTheme theme) {
        final SolidDirectoryQuery sdirectory = new SolidDirectoryQuery(new Storage(getContext()), new FocAndroidFactory(getContext()));

        LinearLayout geo = new LinearLayout(getContext());

        geo.addView(new SolidCheckBox(getContext(),sdirectory.getUseGeo(), theme));
        geo.addView(new SolidBoundingBoxView(getContext(), sdirectory.getBoundingBox(), mc, theme));
        layout.addView(geo);

        LinearLayout from = new LinearLayout(getContext());
        //AppTheme.alt.background(from);
        from.addView(new SolidCheckBox(getContext(),sdirectory.getUseDateStart(), theme));
        from.addView(new SolidDateView(getContext(),sdirectory.getDateStart(), theme));
        layout.addView(from);

        LinearLayout to = new LinearLayout(getContext());
        //AppTheme.alt.background(to);
        to.addView(new SolidCheckBox(getContext(), sdirectory.getUseDateEnd(), theme));
        to.addView(new SolidDateView(getContext(), sdirectory.getDateTo(), theme));
        layout.addView(to);
    }

}
