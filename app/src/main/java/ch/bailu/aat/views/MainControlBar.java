package ch.bailu.aat.views;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.TrackerActivity;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.menus.OptionsMenu;
import ch.bailu.aat.services.ServiceContext;
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

    public void addAll(MultiView mv) {
        add(new MvPreviousButton(mv));
        add(new MvListButton(mv));
        add(new MvNextButton(mv));
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

}
