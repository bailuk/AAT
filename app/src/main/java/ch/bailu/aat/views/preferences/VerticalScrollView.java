package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.osmdroid.views.MapView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.DispatcherInterface;
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


    public void addAllContent(DispatcherInterface di,
                              ContentDescription[] descriptions,
                              int iid) {
        final LabelTextView views[] = new LabelTextView[descriptions.length];
        for (int i=0; i< descriptions.length; i++) {
            views[i] = new LabelTextView(getContext(),descriptions[i]);
            add(views[i]);

            di.addTarget(views[i], iid);
        }
    }


    public void addAllFilterViews(MapView map) {
        final SolidDirectoryQuery sdirectory = new SolidDirectoryQuery(map.getContext());

        LinearLayout geo = new LinearLayout(map.getContext());
        geo.addView(new SolidCheckBox(sdirectory.getUseGeo()));
        geo.addView(new SolidBoundingBoxView(sdirectory.getBoundingBox(), map));
        layout.addView(geo);


        LinearLayout from = new LinearLayout(map.getContext());
        from.addView(new SolidCheckBox(sdirectory.getUseDateStart()));
        from.addView(new SolidDateView(sdirectory.getDateStart()));
        layout.addView(from);

        LinearLayout to = new LinearLayout(map.getContext());
        to.addView(new SolidCheckBox(sdirectory.getUseDateEnd()));
        to.addView(new SolidDateView(sdirectory.getDateTo()));
        layout.addView(to);
    }
}
