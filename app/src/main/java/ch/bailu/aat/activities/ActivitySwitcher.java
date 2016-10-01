package ch.bailu.aat.activities;

import java.io.File;

import org.osmdroid.util.BoundingBoxE6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppIntent;

public class ActivitySwitcher {
    public final static ActivitySwitcher list[] = {
            new ActivitySwitcher(R.string.intro_resume, TrackerActivity.class),
            new ActivitySwitcher(R.string.intro_cockpit2, SplitViewActivity.class),
            new ActivitySwitcher(R.string.intro_map, MapActivity.class),
            new ActivitySwitcher(R.string.intro_list, TrackListActivity.class),
            new ActivitySwitcher(R.string.intro_overlay_list, OverlayListActivity.class),
            new ActivitySwitcher(R.string.intro_import_list, ImportListActivity.class),
            new ActivitySwitcher(R.string.intro_settings, PreferencesActivity.class),
            new ActivitySwitcher(R.string.intro_readme, ReadmeActivity.class),
            new ActivitySwitcher(R.string.intro_about, AboutActivity.class),
            new ActivitySwitcher(R.string.intro_test, TestActivity.class),
    };


    private final int      activityLabel;
    private final Class<?> activityClass;

    public final static int cycable = 3;

    public ActivitySwitcher(int label, Class<?> c) {
        activityLabel=label;
        activityClass=c;
    }

    public int getLabel() {
        return activityLabel;
    }

    public void start(Context c) {
        start(c, activityClass);
    }


    public static void start(Context context, Class<?> activityClass) {
        start(context, activityClass, activityClass.getSimpleName());
    }


    public static void start(Context context, Class<?> activityClass, String string) {
        Intent intent = new Intent();

        AppIntent.setFile(intent, string);
        start(context, activityClass, intent);
    }




    public static void start(Context context, Class<?> activityClass, File file) {
        Intent intent = new Intent();

        AppIntent.setFile(intent, file.getAbsolutePath());
        start(context, activityClass, intent);
    }




    public static void start(Context context, Class<?> activityClass, Intent intent) {
        intent.setClass(context, activityClass);
        intent.setAction(activityClass.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    public static void cycle(Activity a) {
        for (int i=0; i<list.length; i++) {
            if (list[i].activityClass.equals(a.getClass())) {
                if (i+1 > cycable-1) {
                    list[0].start(a);
                } else {
                    list[i+1].start(a);
                }
                a.finish();
                break;
            }
        }
    }

    public static void start(Context context, Class<?> a, BoundingBoxE6 box) {
        Intent intent = new Intent();
        AppIntent.setBoundingBox(intent, box);
        start(context, a, intent);

    }

}
