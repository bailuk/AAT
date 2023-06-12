package ch.bailu.aat.views.tileremover;

import android.app.Activity;
import android.widget.RadioGroup;

import java.util.ArrayList;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.SolidTrimIndex;
import ch.bailu.aat.services.tileremover.SourceSummaries;
import ch.bailu.aat.util.ui.UiTheme;

public class TileSummariesView extends RadioGroup  {

    private final ArrayList<TileSummaryView> views = new ArrayList<>(SourceSummaries.SUMMARY_SIZE);
    private final StringBuilder builder = new StringBuilder(100);


    private final UiTheme theme;

    public TileSummariesView(Activity a, UiTheme theme) {
        super(a);
        setOrientation(VERTICAL);
        this.theme = theme;
    }


    public void updateInfo(final SourceSummaries summaries) {

        if (summaries.size() != views.size()) {
            int selected = new SolidTrimIndex(new Storage(getContext())).getValue();


            // remove views
            for (int i = views.size() - 1; i >= summaries.size(); i--) {
                views.get(i).destroy();
                views.remove(i);
            }


            // addLayer views
            for (int i = views.size(); i < summaries.size(); i++) {
                views.add(new TileSummaryView(this, i, theme));
                views.get(i).select(selected);
            }


            // update title
            for (int i = 0; i< summaries.size() && i < views.size(); i++) {
                views.get(i).setTitle(summaries.get(i).getName());
            }

            if (selected >= summaries.size()) {
                views.get(0).select();
                new SolidTrimIndex(new Storage(getContext())).setValue(0);
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
