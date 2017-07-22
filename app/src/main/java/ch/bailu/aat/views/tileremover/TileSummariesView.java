package ch.bailu.aat.views.tileremover;

import android.app.Activity;
import android.widget.RadioGroup;

import java.util.ArrayList;

import ch.bailu.aat.preferences.SolidTrimIndex;
import ch.bailu.aat.services.tileremover.SourceSummaries;

public class TileSummariesView extends RadioGroup  {

    private final ArrayList<TileSummaryView> views = new ArrayList<>(SourceSummaries.SUMMARY_SIZE);
    private final StringBuilder builder = new StringBuilder(100);


    public TileSummariesView(Activity a) {
        super(a);
        setOrientation(VERTICAL);
    }


    public void updateInfo(final SourceSummaries summaries) {

        if (summaries.size() != views.size()) {
            int selected = new SolidTrimIndex(getContext()).getValue();


            // remove views
            for (int i = views.size() - 1; i >= summaries.size(); i--) {
                views.get(i).destroy();
                views.remove(i);
            }


            // addLayer views
            for (int i = views.size(); i < summaries.size(); i++) {
                views.add(new TileSummaryView(this, i));
                views.get(i).select(selected);
            }


            // update title
            for (int i = 0; i< summaries.size() && i < views.size(); i++) {
                views.get(i).setTitle(summaries.get(i).getName());
            }

            if (selected >= summaries.size()) {
                views.get(0).select();
                new SolidTrimIndex(getContext()).setValue(0);
            }
        }

        if (summaries.size() == views.size()) {
            // update text
            for (int i = 0; i < views.size(); i++) {
                views.get(i).displaySummaryReport(builder, summaries.get(i));
            }
        }
    }
}
