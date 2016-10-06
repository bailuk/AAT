package ch.bailu.aat.views.description;

import android.content.Context;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.AbsLabelTextView;

public class LabelTextView extends AbsLabelTextView implements DescriptionInterface{
    private final ContentDescription description;

    public LabelTextView(Context context, ContentDescription d) {
        super(context, d.getLabel());

        description = d;
        setText();
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        description.updateGpxContent(info);
        setText();
    }


    public void setText() {
        setText(description.getValue()+" "+ description.getUnit());
    }
}
