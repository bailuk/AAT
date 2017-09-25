package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.widget.TextView;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.util_java.foc.Foc;

public class SolidDirectoryView extends AbsSolidView {
    protected final SolidFile solid;


    private final TextView permission;


    public SolidDirectoryView(SolidFile s) {
        super(s);
        solid = s;

        permission = new TextView(getContext());
        permission.setTextColor(AppTheme.getHighlightColor3());

        addView(permission);
    }


    @Override
    public void onRequestNewValue() {
        new SolidStringDialog(solid);
    }


    @Override
    public void setText() {
        super.setText();
        permission.setText(getPermissionText(getContext(), solid.getValueAsFile()));
    }

    private static String getPermissionText(Context c, Foc f) {

        if (f.exists() == false) {
            if (f.hasParent()) {
                return getPermissionText(c, f.parent());
            } else {
                return f.getPathName() + c.getString(R.string.file_is_missing);
            }
        } else if (f.canWrite()) {
            if (f.canRead()) {
                return f.getPathName() + c.getString(R.string.file_is_writeable);
            } else {
                return f.getPathName() + " is write only!";
            }
        } else if (f.canRead()) {
            return f.getPathName() + c.getString(R.string.file_is_readonly);
        } else if (f.hasParent()){
            return getPermissionText(c, f.parent());
        } else  {
            return f.getPathName() + c.getString(R.string.file_is_denied);
        }
    }

}
