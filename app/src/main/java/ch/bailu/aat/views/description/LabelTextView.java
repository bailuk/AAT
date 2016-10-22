package ch.bailu.aat.views.description;

import android.content.Context;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.AbsLabelTextView;

public class LabelTextView extends AbsLabelTextView implements OnContentUpdatedInterface {
    private final ContentDescription description;

    public LabelTextView(Context context, ContentDescription d) {
        super(context, d.getLabel());

        description = d;
        setText();
    }

    @Override
    public void onContentUpdated(GpxInformation info) {
        description.onContentUpdated(info);
        setText();
    }


    public void setText() {
        setText(description.getValueAsString());
    }
}
