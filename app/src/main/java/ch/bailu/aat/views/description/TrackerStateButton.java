package ch.bailu.aat.views.description;

import android.view.View;
import android.view.View.OnClickListener;

import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppTheme;

public class TrackerStateButton extends ColorNumberView implements OnClickListener {

    final private ServiceContext scontext; 
    
    
    public TrackerStateButton(ServiceContext c) {
        super(new TrackerStateDescription(c.getContext()), AppTheme.bar);

        scontext=c;

        setOnClickListener(this);

        AppTheme.bar.button(this);
        setPadding(0,0,0,0);
    }

    @Override
    public void onClick(View v) {
        if (v==this) {
            scontext.getTrackerService().getState().onStartPauseResume();
        }
    }
}
