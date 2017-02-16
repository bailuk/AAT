package ch.bailu.aat.views;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.menus.MultiViewMenu;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.menus.OptionsMenu;
import ch.bailu.aat.views.description.GPSStateButton;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.TrackerStateButton;

public class MainControlBar extends ControlBar {

    private final BusyButton menu;

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

        menu = addMenuButton(acontext);
        addBackButton(acontext);

    }


    private void addBackButton(final AbsDispatcher acontext) {
        if (AppLayout.haveExtraSpaceBack(getContext())) {
            ImageButton b = addImageButton(R.drawable.edit_undo_inverse);
            b.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    acontext.onBackPressed();
                }
            });
        }
    }


    private BusyButton addMenuButton(final AbsDispatcher acontext) {
        final BusyButton menu = new BusyButton(acontext, ch.bailu.aat.R.drawable.open_menu_inverse);
        add(menu);

        menu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new OptionsMenu(acontext.getServiceContext()).showAsPopup(getContext(), menu);
            }});
        return menu;
    }

    public BusyButton getMenu() {
        return menu;
    }


    public void addAll(final MultiView mv) {
        addMvPrevious(mv);
        addMvList(mv);
        addMvNext(mv);
    }

    public void addMvList(final MultiView mv) {
        final MultiViewMenu menu = new MultiViewMenu(mv);
        final ImageButton b = addImageButton(R.drawable.content_loading_inverse);

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.showAsPopup(mv.getContext(), b);
            }
        });
    }
    public void addMvNext(final MultiView mv) {
        addImageButton(R.drawable.go_next_inverse).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.setNext();
            }
        });
    }


    public void addMvPrevious(final MultiView mv) {
        addImageButton(R.drawable.go_previous_inverse).setOnClickListener(new OnClickListener() {
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
        ImageButton cb = addImageButton(R.drawable.go_down_inverse);
        cb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new ActivitySwitcher(acontext).cycle();
            }});
    }
}
