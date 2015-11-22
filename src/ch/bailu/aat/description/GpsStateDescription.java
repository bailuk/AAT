package ch.bailu.aat.description;

import android.content.Context;
import ch.bailu.aat.R;

public class GpsStateDescription extends StateDescription {
	
	   public GpsStateDescription(Context c) {
		super(c);
	}

	@Override
	    public String getLabel() {
		   return getString(R.string.gps);
	    }
	
}
