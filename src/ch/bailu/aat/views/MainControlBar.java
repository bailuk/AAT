package ch.bailu.aat.views;

import android.app.Activity;
import android.view.View;
import ch.bailu.aat.helpers.AppLayout;

public class MainControlBar extends ControlBar {

    public MainControlBar(final Activity context) {
        this(context, DEFAULT_VISIBLE_BUTTON_COUNT);
    }
    
    
    public MainControlBar(final Activity context, int button) {
        super(context, AppLayout.getOrientationAlongSmallSide(context), button);
        
        addImageButton(ch.bailu.aat.R.drawable.open_menu_inverse).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context.openOptionsMenu();
            }});
        
    }

    
}
