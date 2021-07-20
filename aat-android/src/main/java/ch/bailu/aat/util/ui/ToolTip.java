package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import ch.bailu.aat_lib.map.MapColor;

public class ToolTip {
    public static void set(View view, Integer resID) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setTooltipText(new ToolTipRes(view.getContext(), resID).getToolTip());
        } else {
            new ToolTipLongClick(view, new ToolTipRes(view.getContext(), resID));
        }
    }


    public static void set(View view, String toolTip) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setTooltipText(toolTip);
        } else {
            new ToolTipLongClick(view, new ToolTipString(toolTip));
        }

    }

    public static void themeify(TextView v) {
        v.setBackgroundColor(MapColor.LIGHT);
        v.setTextColor(Color.BLACK);
        AppTheme.padding(v,10);

    }
}
