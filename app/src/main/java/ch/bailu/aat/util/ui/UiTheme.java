package ch.bailu.aat.util.ui;

import android.view.View;
import android.widget.TextView;

public interface UiTheme {

    float TEXT_SIZE = 18f;
    int TT_COLOR = AppTheme.COLOR_BLUE;

    void background(View v);
    void button(View v);

    void topic(TextView v);
    void header(TextView v);
    void content(TextView v);
    void toolTip(TextView v);

}
