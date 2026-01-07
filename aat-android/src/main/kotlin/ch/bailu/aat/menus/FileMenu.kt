package ch.bailu.aat.menus

import android.view.Menu
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.foc.Foc

open class FileMenu(aContext: ActivityContext, file: Foc) : AbsFileMenu(aContext, file) {
    override fun inflate(menu: Menu) {
        inflateOverlay(menu)
        inflateCopyToSelect(menu)
        inflateManipulate(menu)
        inflateCopySend(menu)
    }
}
