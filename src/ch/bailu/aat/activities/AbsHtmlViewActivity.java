package ch.bailu.aat.activities;

import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.ScrollView;
import android.widget.TextView;
import ch.bailu.aat.helpers.AppTheme;

public abstract class AbsHtmlViewActivity extends AbsServiceLink {
    
    private TextView text;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scroll=new ScrollView(this);

        text = new TextView(this);
        text.setTextSize(15f);
        text.setAutoLinkMask(Linkify.ALL);
        text.setLinkTextColor(AppTheme.getHighlightColor());
        
        scroll.addView(text);
        setContentView(scroll);
    }

    
    
    public void setText(int mask) {
        text.setAutoLinkMask(mask);
        setText();
    }
    
    
    public void setText() {
        text.setText(Html.fromHtml(getText()));
    }
    
    public abstract String getText();
}
