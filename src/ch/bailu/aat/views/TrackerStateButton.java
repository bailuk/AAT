package ch.bailu.aat.views;

import android.view.View;
import android.view.View.OnClickListener;
import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;

public class TrackerStateButton extends NumberView implements OnClickListener {

    final private ServiceContext scontext; 
    public TrackerStateButton(ServiceContext c) {
        super(new TrackerStateDescription(c.getContext()), 
                GpxInformation.ID.INFO_ID_TRACKER);
        
        scontext=c;
        setBackgroundResource(ch.bailu.aat.R.drawable.button_alt);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==this) {
            scontext.getTrackerService().getState().onStartPauseResume();
        }
    }

    
}
