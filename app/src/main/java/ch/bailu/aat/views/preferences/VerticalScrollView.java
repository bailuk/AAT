package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
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
        layout.addView(view);
    }

    public void add(DispatcherInterface di, ContentDescription d, int iid) {
        final LabelTextView v = new LabelTextView(getContext(), d);

        add(v);
        di.addTarget(v, iid);

    }


    public void addAllContent(DispatcherInterface di,
                              ContentDescription[] descriptions,
                              int iid) {
        for (int i=0; i< descriptions.length; i++) {
            add(di, descriptions[i], iid);
        }
    }


    public void addAllFilterViews(MapContext mc) {
        final SolidDirectoryQuery sdirectory = new SolidDirectoryQuery(mc.getContext());

        LinearLayout geo = new LinearLayout(mc.getContext());
        geo.addView(new SolidCheckBox(sdirectory.getUseGeo()));
        geo.addView(new SolidBoundingBoxView(sdirectory.getBoundingBox(), mc));
        layout.addView(geo);


        LinearLayout from = new LinearLayout(mc.getContext());
        from.addView(new SolidCheckBox(sdirectory.getUseDateStart()));
        from.addView(new SolidDateView(sdirectory.getDateStart()));
        layout.addView(from);

        LinearLayout to = new LinearLayout(mc.getContext());
        to.addView(new SolidCheckBox(sdirectory.getUseDateEnd()));
        to.addView(new SolidDateView(sdirectory.getDateTo()));
        layout.addView(to);
    }

}
