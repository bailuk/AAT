package ch.bailu.aat.activities;

import ch.bailu.aat.helpers.AppBroadcaster;
import android.content.Intent;
import android.os.Bundle;

public class HtmlViewActivity extends AbsHtmlViewActivity {

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setText();
    }

    
    @Override
    public String getText() {
        Intent intent = getIntent();
        return AppBroadcaster.getFile(intent);
    }

    
    @Override
    public void onServicesUp(boolean firstRun) {}
}
