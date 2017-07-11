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
        return getContext().getString(R.string.d_state);
    }

    public String getValue() {
        String value;
        
        switch (state) {
        case StateID.NOACCESS: value=getContext().getString(R.string.gps_noaccess); break;
        case StateID.NOSERVICE: value=getContext().getString(R.string.gps_nogps); break;
        case StateID.ON: value=getContext().getString(R.string.on); break;
        case StateID.OFF: value=getContext().getString(R.string.off); break;
        case StateID.PAUSE: value=getContext().getString(R.string.status_paused); break;
        case StateID.AUTOPAUSED: value=getContext().getString(R.string.status_autopaused); break;
        default: value=getContext().getString(R.string.gps_wait); break;
        }
        return value;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        state=info.getState();
    }
}
