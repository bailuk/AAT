package ch.bailu.aat.views;

import android.view.View;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.MultiViewMenu;
import ch.bailu.aat.views.description.MultiView;

public class MvListButton extends ImageButtonView {

    public MvListButton(final MultiView mv) {
        super(mv.getContext(), R.drawable.content_loading_inverse);

        final MultiViewMenu menu = new MultiViewMenu(mv);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.showAsPopup(mv.getContext(), MvListButton.this);
            }
        });
    }
}
