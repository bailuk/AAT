package ch.bailu.aat.views.bar;

import android.app.Activity;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.MainActivity;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.menus.OptionsMenu;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.description.mview.MultiViewNextButton;
import ch.bailu.aat.views.description.mview.MultiViewSelector;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.description.GPSStateButton;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.description.SensorStateButton;
import ch.bailu.aat.views.description.TrackerStateButton;
import ch.bailu.util_java.util.Objects;

public class MainControlBar extends ControlBar {


    public MainControlBar(final AbsDispatcher acontext) {
        this(acontext, AppLayout.DEFAULT_VISIBLE_BUTTON_COUNT);
    }

    public MainControlBar(final AbsDispatcher acontext, final MultiView mv) {
        this(acontext);
        addAll(mv);
    }

    public MainControlBar(final AbsDispatcher acontext, int button) {
        this(acontext, LinearLayout.HORIZONTAL, button);
    }


    public MainControlBar(final AbsDispatcher acontext, int orientation, int button) {
        super(acontext, orientation, button, AppTheme.bar);

        if (Objects.equals(acontext.getClass().getSimpleName(), MainActivity.class.getSimpleName()))
            addMenuButton(acontext);
        else
            addBackButton(acontext);

    }


    private void addBackButton(final AbsDispatcher acontext) {
        ImageButtonViewGroup b = addImageButton(R.drawable.edit_undo_inverse, getControlSize());
        b.setOnClickListener(v -> acontext.onBackPressedMenuBar());
    }


    private ImageButtonViewGroup addMenuButton(final AbsDispatcher acontext) {
        final ImageButtonViewGroup menu = addImageButton(ch.bailu.aat.R.drawable.open_menu_inverse);


        menu.setOnClickListener(v -> new OptionsMenu(acontext.getServiceContext()).showAsPopup(getContext(), menu));
        return menu;
    }


    public void addAll(final MultiView mv) {
        addMvPrevious(mv,getControlSize()/2);
        add(new MultiViewSelector(mv), getControlSize()*2);
        addMvNext(mv, getControlSize()/2);
    }


    public void addMvNext(final MultiView mv, int size) {
        add(new MultiViewNextButton(mv, theme), size);
    }


    public void addMvPrevious(final MultiView mv, int size) {
        addImageButton(R.drawable.go_previous_inverse, size).setOnClickListener(v -> mv.setPrevious());
    }

    public MainControlBar addGpsState(AbsDispatcher acontext) {
        GPSStateButton gps = new GPSStateButton(acontext);
        add(gps);
        acontext.addTarget(gps, InfoID.LOCATION);

        return this;
    }

    public MainControlBar addTrackerState(AbsDispatcher acontext) {
        TrackerStateButton ts = new TrackerStateButton(acontext.getServiceContext());
        add(ts);
        acontext.addTarget(ts, InfoID.TRACKER);

        return this;
    }


    public void addSensorState(ActivityContext acontext) {
        SensorStateButton s = new SensorStateButton(acontext.getServiceContext());
        add(s);
        acontext.addTarget(s, InfoID.SENSORS);
    }


    public void addActivityCycle(final Activity acontext) {
        ImageButtonViewGroup cb = addImageButton(R.drawable.go_down_inverse, getControlSize());
        cb.setOnClickListener(v -> new ActivitySwitcher(acontext).cycle());
    }

    public void addMvNext(MultiView mv) {
        addMvNext(mv, getControlSize());
    }
}
