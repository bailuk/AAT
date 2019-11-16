package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;

public class TrackerStateDescription extends StateDescription {


    public TrackerStateDescription(Context c) {
		super(c);
	}

	@Override
    public String getLabel() {
        return getContext().getString(R.string.tracker);
    }

}
