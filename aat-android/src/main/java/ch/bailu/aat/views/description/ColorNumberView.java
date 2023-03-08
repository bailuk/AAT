package ch.bailu.aat.views.description;

import android.content.Context;

import javax.annotation.Nonnull;

import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;

public class ColorNumberView extends NumberView {
    private int state = StateID.OFF;

    public ColorNumberView(Context context, ContentDescription c, UiTheme theme) {
        super(context, c, theme);
    }

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
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
