package ch.bailu.aat.views;

import android.content.SharedPreferences;
import android.view.View;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidIndexList;


public class SolidImageButton extends ImageButtonView implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final SolidIndexList slist;
    
    public SolidImageButton(SolidIndexList s) {
        super(s.getContext(), s.getIconResource());
        
        slist = s;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                slist.cycle();
            }

        });
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setImageResource(slist.getIconResource());
        slist.register(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (slist.hasKey(key)) {
            setImageResource(slist.getIconResource());
            AppLog.i(getContext(), slist.getValueAsString());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        slist.unregister(this);
    }
}
