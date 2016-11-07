package ch.bailu.aat.views.description;

import android.view.View;
import android.view.View.OnClickListener;

import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.services.ServiceContext;

public class TrackerStateButton extends NumberButton implements OnClickListener {

    final private ServiceContext scontext; 
    
    
    public TrackerStateButton(ServiceContext c) {
        super(new TrackerStateDescription(c.getContext()));
        
        scontext=c;

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==this) {
            scontext.getTrackerService().getState().onStartPauseResume();
        }
    }
}
