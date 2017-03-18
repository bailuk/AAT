package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.view.View;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.DirectoryMenu;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.views.ImageButtonView;

public class SolidDirectoryMenuButton extends ImageButtonView {
    public SolidDirectoryMenuButton(final Activity acontext, final SolidDirectory s) {
        super(s.getContext(), R.drawable.folder_inverse);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DirectoryMenu(acontext, s).showAsPopup(
                        s.getContext(),
                        SolidDirectoryMenuButton.this);
            }
        });
    }
}
