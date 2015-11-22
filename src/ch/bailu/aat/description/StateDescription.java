package ch.bailu.aat.description;

import android.content.Context;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.R;

public abstract class StateDescription extends ContentDescription 
implements GpxInformation.ID{

    
	public StateDescription(Context c) {
		super(c);
	}

	private int state=STATE_OFF;

    @Override
    public String getLabel() {
        return "State*";
    }

    @Override
    public String getValue() {
        String value;
        
        switch (state) {
        case STATE_NOACCESS: value=getString(R.string.gps_noaccess); break;
        case STATE_NOSERVICE: value=getString(R.string.gps_nogps); break;
        case STATE_ON: value=getString(R.string.on); break;
        case STATE_OFF: value=getString(R.string.off); break;
        case STATE_PAUSE: value=getString(R.string.status_paused); break;
        case STATE_AUTOPAUSED: value=getString(R.string.status_autopaused); break;
        default: value=getString(R.string.gps_wait); break;
        }
        return value;
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        state=info.getState();
    }
}
