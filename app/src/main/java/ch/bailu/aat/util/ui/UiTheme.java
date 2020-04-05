package ch.bailu.aat.util.ui;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public interface UiTheme {

    float HEADER_TEXT_SIZE = 18f;

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

    int getGraphBackgroundColor();
    int getGraphLineColor();
    int getGraphTextColor();

    void list(ListView l);



}
