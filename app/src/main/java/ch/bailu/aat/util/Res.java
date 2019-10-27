package ch.bailu.aat.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Res {

    public static String s(Context c, int r) {
        return c.getString(r);
    }

    public static Drawable d(Context c, int r) {
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = c.getResources().getDrawable(r, c.getTheme());
        } else {
            drawable = c.getResources().getDrawable(r);
        }
        return drawable;
    }
}
