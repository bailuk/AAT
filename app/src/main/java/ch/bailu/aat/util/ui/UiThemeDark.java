package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UiThemeDark implements UiTheme {


    public final static int CODE_BG = 0xFFF3F3F3;

    private final static int HIGHLIGHT = AppTheme.COLOR_ORANGE;
    private final static int TEXT = 0xFF24292e;
    private final static int LINK = 0xFF428ce0;
    private final static int BACKGROUND = Color.BLACK;
    private final static int ALT_BACKGOUND=Color.DKGRAY;
    private final static int BUTTON_PRESSED = 0x88136fd8;
    private final static int BUTTON = 0x88f3f3f3;


    @Override
    public int getBackgroundColor() {
        return BACKGROUND;

    }

    @Override
    public int getHighlightColor () {
        return HIGHLIGHT;
    }


    @Override
    public void list(ListView l) {
        int height = l.getDividerHeight();
        l.setDivider(new ColorDrawable(HIGHLIGHT));
        l.setDividerHeight(height);
        l.setSelector(android.R.color.transparent);
    }

    @Override
    public void background(View v) {
        v.setBackgroundColor(BACKGROUND);
    }

    @Override
    public void button(View v) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(0, HIGHLIGHT));
    }


    @Override
    public void topic(TextView v) {
        v.setTextColor(HIGHLIGHT);
        v.setTextSize(TEXT_SIZE);
    }

    @Override
    public void header(TextView v) {
        v.setTextColor(Color.WHITE);
        v.setTextSize(TEXT_SIZE);
    }

    @Override
    public void content(TextView v) {
        v.setTextColor(Color.LTGRAY);
        v.setLinkTextColor(HIGHLIGHT);
    }

    @Override
    public void toolTip(TextView v) {
        v.setTextColor(TT_COLOR);
    }


    @Override
    public void backgroundAlt(View v) {
        v.setBackgroundColor(ALT_BACKGOUND);
    }


    @Override
    public void contentAlt(TextView v) {
        v.setTextColor(Color.WHITE);
    }


}
