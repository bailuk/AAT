package ch.bailu.aat.activities;

import android.os.Bundle;
import ch.bailu.aat.R;


public class AboutActivity extends AbsHtmlViewActivity {
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setText();
    }

    
    @Override
    public void onServicesUp(boolean firstRun) {}

    
    @Override
    public String getText() {
        return getString(R.string.README_about_html);
    }


    @Override
    public void onResumeWithService() {
    }
    

}