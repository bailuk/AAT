package ch.bailu.aat.views;

import ch.bailu.aat.helpers.AppLayout;
import android.content.Context;
import android.widget.LinearLayout;

public class ContentView extends LinearLayout {

    public ContentView(Context context) {
        this(context, AppLayout.getOrientationAlongLargeSide(context));
    }

    
    public ContentView(Context context, int o) {
        super(context);
        setOrientation(o);
    }
}
