package ch.bailu.aat.views.description;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;

public class ColorNumberView extends NumberView {
    private int state = StateID.OFF;

    public ColorNumberView(ContentDescription c) {
        super(c);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        super.onContentUpdated(iid, info);

        if (state != info.getState()) {
            state = info.getState();

            if (state == StateID.ON) {
                setHighlightUnitLabelColor();
            } else {
                setDefaultUnitLabelColor();
            }
        }
    }



}
