package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;

public abstract class StateDescription extends ContentDescription {

    
	public StateDescription(Context c) {
		super(c);
	}

	private int state= StateID.OFF;

    @Override
    public String getLabel() {
        return getString(R.string.d_state);
    }

    public String getTime() {
        String value;
        
        switch (state) {
        case StateID.NOACCESS: value=getString(R.string.gps_noaccess); break;
        case StateID.NOSERVICE: value=getString(R.string.gps_nogps); break;
        case StateID.ON: value=getString(R.string.on); break;
        case StateID.OFF: value=getString(R.string.off); break;
        case StateID.PAUSE: value=getString(R.string.status_paused); break;
        case StateID.AUTOPAUSED: value=getString(R.string.status_autopaused); break;
        default: value=getString(R.string.gps_wait); break;
        }
        return value;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        state=info.getState();
    }
}
