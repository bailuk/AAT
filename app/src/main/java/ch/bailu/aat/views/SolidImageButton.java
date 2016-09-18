package ch.bailu.aat.views;

import android.view.View;
import android.widget.ImageButton;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidIndexList;


public class SolidImageButton extends ImageButton {

    private final SolidIndexList sList;
    
    public SolidImageButton(SolidIndexList sgrid) {
        super(sgrid.getContext());
        
        sList = sgrid;
        setOnClickListener(onClick);
        
        setImage();
    }


    void onPreferencesChanged(String key) {
        if (sList.hasKey(key)) {
            setImage();
            AppLog.i(getContext(), sList.getString());
        }
    }
    
    
    private void setImage() {
        setImageResource(sList.getImageResource());
    }

    
    private final OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            sList.cycle();
        }
        
    };
}
