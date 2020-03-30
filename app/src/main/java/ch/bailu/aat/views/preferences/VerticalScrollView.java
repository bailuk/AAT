package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.description.LabelTextView;


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
        final LabelTextView v = new LabelTextView(getContext(), d, theme);

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
        final SolidDirectoryQuery sdirectory = new SolidDirectoryQuery(mc.getContext());

        LinearLayout geo = new LinearLayout(mc.getContext());

        geo.addView(new SolidCheckBox(sdirectory.getUseGeo(), theme));
        geo.addView(new SolidBoundingBoxView(sdirectory.getBoundingBox(), mc, theme));
        layout.addView(geo);

        LinearLayout from = new LinearLayout(mc.getContext());
        //AppTheme.alt.background(from);
        from.addView(new SolidCheckBox(sdirectory.getUseDateStart(), theme));
        from.addView(new SolidDateView(sdirectory.getDateStart(), theme));
        layout.addView(from);

        LinearLayout to = new LinearLayout(mc.getContext());
        //AppTheme.alt.background(to);
        to.addView(new SolidCheckBox(sdirectory.getUseDateEnd(), theme));
        to.addView(new SolidDateView(sdirectory.getDateTo(), theme));
        layout.addView(to);
    }

}
