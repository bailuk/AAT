package ch.bailu.aat.views.description;

import android.view.View;
import android.view.View.OnClickListener;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat_lib.description.TrackerStateDescription;

public class TrackerStateButton extends ColorNumberView implements OnClickListener {

    final private ServiceContext scontext;


    public TrackerStateButton(ServiceContext c) {
        super(c.getContext(),new TrackerStateDescription(), AppTheme.bar);

        scontext=c;

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==this) {
            new InsideContext(scontext) {

                @Override
                public void run() {
                    scontext.getTrackerService().getState().onStartPauseResume();
                }
            };

        }
    }
}
