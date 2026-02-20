package ch.bailu.aat.menus

import android.view.Menu
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.foc.Foc

class ResultFileMenu(aContext: ActivityContext, file: Foc) : FileMenu(aContext, file) {
    override fun inflate(menu: Menu) {
        inflateCopyTo(menu)
        inflateCopySend(menu)
    }

}
