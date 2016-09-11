package ch.bailu.aat.helpers;

import android.view.View;
import android.view.View.OnLongClickListener;

public class ToolTip {
    public static void set(View v, int toolTipResource) {
        Integer id=Integer.valueOf(toolTipResource);
        
        v.setLongClickable(true);
        v.setOnLongClickListener(onLongClickListener);
        v.setTag(id);
    }
    
    
    private static final OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            
            if (v.getTag() != null) {
                Integer toolTipResource = (Integer)v.getTag();
                String toolTipText=v.getContext().getString(toolTipResource);
                AppLog.i(v.getContext(), toolTipText);
                return true;
            }
            return false;
        }    
    };

}
