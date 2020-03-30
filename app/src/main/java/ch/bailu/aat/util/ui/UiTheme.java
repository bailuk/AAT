package ch.bailu.aat.util.ui;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.bar.ControlBar;

public interface UiTheme {

    float TEXT_SIZE = 18f;
    int TT_COLOR = AppTheme.COLOR_BLUE;

    void background(View v);
    void backgroundAlt(View v);

    void button(View v);

    void topic(TextView v);
    void header(TextView v);

    void content(TextView v);
    void contentAlt(TextView v);

    void toolTip(TextView v);

    int getBackgroundColor();
    int getHighlightColor();

    void list(ListView l);



}
