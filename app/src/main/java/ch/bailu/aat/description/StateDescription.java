package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public abstract class StateDescription extends ContentDescription {

    
	public StateDescription(Context c) {
		super(c);
	}

	private int state=GpxInformation.ID.STATE_OFF;

    @Override
    public String getLabel() {
        return getString(R.string.d_state);
    }

    @Override
    public String getValue() {
        String value;
        
        switch (state) {
        case GpxInformation.ID.STATE_NOACCESS: value=getString(R.string.gps_noaccess); break;
        case GpxInformation.ID.STATE_NOSERVICE: value=getString(R.string.gps_nogps); break;
        case GpxInformation.ID.STATE_ON: value=getString(R.string.on); break;
        case GpxInformation.ID.STATE_OFF: value=getString(R.string.off); break;
        case GpxInformation.ID.STATE_PAUSE: value=getString(R.string.status_paused); break;
        case GpxInformation.ID.STATE_AUTOPAUSED: value=getString(R.string.status_autopaused); break;
        default: value=getString(R.string.gps_wait); break;
        }
        return value;
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        state=info.getState();
    }
}
