package ch.bailu.aat.views.description;

import android.content.Context;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.AbsLabelTextView;

public class LabelTextView extends AbsLabelTextView implements OnContentUpdatedInterface {
    private final ContentDescription description;

    public LabelTextView(Context context, ContentDescription d, UiTheme theme) {
        super(true, context, d.getLabel(), theme);

        description = d;
        setText();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        description.onContentUpdated(iid, info);
        setText();
    }


    public void setText() {
        setText(description.getValueAsString());
    }
}
