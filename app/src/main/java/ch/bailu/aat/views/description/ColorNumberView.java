package ch.bailu.aat.views.description;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.util.ui.UiTheme;

public class ColorNumberView extends NumberView {
    private int state = StateID.OFF;

    public ColorNumberView(ContentDescription c, UiTheme theme) {
        super(c, theme);
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
