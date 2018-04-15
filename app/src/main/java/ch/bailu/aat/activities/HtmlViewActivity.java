package ch.bailu.aat.activities;

import android.content.Intent;
import android.os.Bundle;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.html.HtmlScrollTextView;
import ch.bailu.aat.views.bar.MainControlBar;

public class HtmlViewActivity extends AbsDispatcher {

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentView contentView = new ContentView(this);

        contentView.add(new MainControlBar(this));
        contentView.add(new HtmlScrollTextView(this, getTextFromIntent()));

        setContentView(contentView);
    }

    
    private String getTextFromIntent() {
        Intent intent = getIntent();
        return AppIntent.getFile(intent);
    }

}
