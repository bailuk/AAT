package ch.bailu.aat.description;

import ch.bailu.aat.R;
import android.content.Context;

public class TrackerStateDescription extends StateDescription {
    

    public TrackerStateDescription(Context c) {
		super(c);
	}

	@Override
    public String getLabel() {
        return getString(R.string.tracker);
    }
    
}
