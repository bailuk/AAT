package ch.bailu.aat.activities;

import android.app.Activity;
import android.os.Bundle;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.PreferenceLoadDefaults;

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
        created++;
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
        builder.append("<p>Instantiated activites: ");
        builder.append(instantiated);
        builder.append("<br>Created activites: ");
        builder.append(created);
        builder.append("<br>Count of aplication starts: ");
        builder.append(PreferenceLoadDefaults.getStartCount(this));
        builder.append("</p>");
    }
}
