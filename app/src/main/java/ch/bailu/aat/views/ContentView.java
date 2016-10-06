package ch.bailu.aat.views;

import android.content.Context;
import android.widget.LinearLayout;

import ch.bailu.aat.helpers.AppLayout;

public class ContentView extends LinearLayout {

    public ContentView(Context context) {
        this(context, AppLayout.getOrientationAlongLargeSide(context));
    }

    
    public ContentView(Context context, int o) {
        super(context);
        setOrientation(o);
    }
}
