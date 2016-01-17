package ch.bailu.aat.activities;

import android.os.Bundle;
import ch.bailu.aat.R;

public class ReadmeActivity extends AbsHtmlViewActivity {

   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setText();
    }
 
    
    @Override
    public void onServicesUp() {}

    
    @Override
    public String getText() {
        return getString(R.string.README_enduser_html);
    }

}
