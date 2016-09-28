package ch.bailu.aat.activities;

import android.content.Intent;
import android.os.Bundle;
import ch.bailu.aat.helpers.AppIntent;

public class HtmlViewActivity extends AbsHtmlViewActivity {

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setText();
    }

    
    @Override
    public String getText() {
        Intent intent = getIntent();
        return AppIntent.getFile(intent);
    }

    
    @Override
    public void onServicesUp(boolean firstRun) {}


    @Override
    public void onResumeWithServices() {}
}
