package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.DirectoryMenu;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.views.ImageButtonView;

public class SolidDirectoryMenuButton extends ImageButtonView {
    public SolidDirectoryMenuButton(final Activity acontext, final SolidFile s) {
        super(s.getContext(), R.drawable.folder_inverse);

        setOnClickListener(v -> new DirectoryMenu(acontext, s).showAsPopup(
                s.getContext(),
                SolidDirectoryMenuButton.this));
    }
}
