package ch.bailu.aat.views.preferences

import ch.bailu.aat.R
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.dialog.SolidTextInputDialog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.beacon.SolidBeaconEnabled
import ch.bailu.aat_lib.preferences.beacon.SolidBeaconServer
import ch.bailu.aat_lib.preferences.beacon.SolidBeaconKey

class NetworkPreferencesView(acontext: ActivityContext, theme: UiTheme) :
    VerticalScrollView(acontext) {
    init {
        val storage: StorageInterface = Storage(acontext)

        add(TitleView(acontext, "Beacon", theme))

        add(SolidCheckBox(acontext, SolidBeaconEnabled(storage), theme))

        add(
            SolidStringView(
                acontext,
                SolidBeaconServer(storage),
                theme
            )
        )

        add(
            SolidTextInputView(
                acontext,
                SolidBeaconKey(storage),
                SolidTextInputDialog.INTEGER,
                theme
            )
        )
    }
}
