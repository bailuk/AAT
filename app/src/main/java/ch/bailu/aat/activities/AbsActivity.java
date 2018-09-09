package ch.bailu.aat.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.preferences.PreferenceLoadDefaults;
import ch.bailu.aat.util.AppPermission;
import ch.bailu.aat.util.ui.AppLog;

public abstract class AbsActivity extends Activity {
    private AppLog logger;

    private static int instantiated=0;
    private static int created=0;


    public AbsActivity() {
        instantiated++;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PreferenceLoadDefaults(this);
        created++;
    }


    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            String[] permissions,
                                            int[] grantResults) {
        AppPermission.onRequestPermissionsResult(this, requestCode);
    }


    @Override
    public void onDestroy() {
        created--;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        logger.close();
        logger=null;
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        logger = new AppLog(this);
    }


    @Override
    protected void finalize () throws Throwable {
        instantiated--;
        super.finalize();
    }

    public void appendStatusText(StringBuilder builder) {
        builder.append("<h1>");
        builder.append(getClass().getSimpleName());
        builder.append("</h1>");
        builder.append("<p>Instantiated activities: ");
        builder.append(instantiated);
        builder.append("<br>Created activities: ");
        builder.append(created);
        builder.append("<br>Count of application starts: ");
        builder.append(PreferenceLoadDefaults.getStartCount(this));
        builder.append("</p>");

    }


    @Override
    public void onBackPressed() {
        View view = getWindow().getDecorView();

        if (onBackPressed(view) == false)
            super.onBackPressed();

    }

    public interface OnBackPressed {
        boolean onBackPressed();
    }

    public abstract static class OnBackPressedListener extends View implements AbsActivity.OnBackPressed {
        public OnBackPressedListener(Context context) {
            super(context);
            setVisibility(INVISIBLE);
        }

        @Override
        public abstract boolean onBackPressed();
    }

    private boolean onBackPressed(View view) {
        if (view instanceof OnBackPressed) {
            if (((OnBackPressed) view).onBackPressed())
                return true;
        }

        if (view instanceof ViewGroup) {
            if (onBackPressedChildren((ViewGroup) view))
                return true;
        }
        return false;
    }

    private boolean onBackPressedChildren(ViewGroup parent) {
        int count = parent.getChildCount();

        for (int i=0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (onBackPressed(view)) return true;
        }
        return false;
    }
}
