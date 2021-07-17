package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.DirectoryMenu;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat_lib.preferences.SolidFile;

public class SolidDirectoryMenuButton extends ImageButtonViewGroup {
    public SolidDirectoryMenuButton(final Activity acontext, final SolidFile s) {
        super(acontext, R.drawable.folder_inverse);

        setOnClickListener(v -> new DirectoryMenu(acontext, s).showAsPopup(
                acontext,
                SolidDirectoryMenuButton.this));
    }
}
