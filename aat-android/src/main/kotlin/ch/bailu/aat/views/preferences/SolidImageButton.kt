package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.generated.Images
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.preferences.dialog.SolidIndexListDialog
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidImageButton(context: Context, private val solid: SolidIndexList) : ImageButtonViewGroup(
    context, Images.get(
        solid.getIconResource()
    )
), OnPreferencesChanged {
    init {
        setOnClickListener {
            if (solid.length() < 3) {
                solid.cycle()
            } else {
                SolidIndexListDialog(context, solid)
            }
        }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setImageResource(Images.get(solid.getIconResource()))
        solid.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            setImageResource(Images.get(solid.getIconResource()))
            AppLog.i(this, solid.getValueAsString())
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        solid.unregister(this)
    }
}
