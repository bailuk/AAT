package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class VerticalScrollView extends ScrollView {
    private final LinearLayout layout;

    public VerticalScrollView(Context context) {
        super(context);

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        addView(layout);
    }


    public void add(View view) {
        layout.addView(view);
    }

}
