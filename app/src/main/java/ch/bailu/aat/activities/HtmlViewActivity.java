package ch.bailu.aat.activities;

import android.content.Intent;
import android.os.Bundle;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.HtmlScrollTextView;
import ch.bailu.aat.views.MainControlBar;

public class HtmlViewActivity extends AbsDispatcher {

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentView contentView = new ContentView(this);

        contentView.addView(new MainControlBar(this));
        contentView.addView(new HtmlScrollTextView(this, getTextFromIntent()));

        setContentView(contentView);
    }

    
    private String getTextFromIntent() {
        Intent intent = getIntent();
        return AppIntent.getFile(intent);
    }

}
