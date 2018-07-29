package ch.bailu.aat.views.bar;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.MainActivity;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.menus.OptionsMenu;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.MultiViewSelector;
import ch.bailu.aat.views.MyImageButton;
import ch.bailu.aat.views.description.GPSStateButton;
import ch.bailu.aat.views.description.MultiView;
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
        super(acontext, orientation, button);

        if (Objects.equals(acontext.getClass().getSimpleName(), MainActivity.class.getSimpleName()))
            addMenuButton(acontext);
        else
            addBackButton(acontext);

    }


    private void addBackButton(final AbsDispatcher acontext) {
        MyImageButton b = addImageButton(R.drawable.edit_undo_inverse, getControlSize());
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                acontext.onBackPressed();
            }
        });
    }


    private MyImageButton addMenuButton(final AbsDispatcher acontext) {
        final MyImageButton menu = addImageButton(ch.bailu.aat.R.drawable.open_menu_inverse);


        menu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new OptionsMenu(acontext.getServiceContext()).showAsPopup(getContext(), menu);
            }});
        return menu;
    }


    public void addAll(final MultiView mv) {
        addMvPrevious(mv,getControlSize()/2);
        add(new MultiViewSelector(mv), getControlSize()*2);
        addMvNext(mv, getControlSize()/2);
    }


    public void addMvNext(final MultiView mv, int size) {
        addImageButton(R.drawable.go_next_inverse, size).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.setNext();
            }
        });
    }


    public void addMvPrevious(final MultiView mv, int size) {
        addImageButton(R.drawable.go_previous_inverse, size).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.setPrevious();
            }
        });
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

    public void addActivityCycle(final Activity acontext) {
        MyImageButton cb = addImageButton(R.drawable.go_down_inverse, getControlSize());
        cb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new ActivitySwitcher(acontext).cycle();
            }});
    }

    public void addMvNext(MultiView mv) {
        addMvNext(mv, getControlSize());
    }
}
