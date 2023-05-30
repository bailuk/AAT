package ch.bailu.aat.activities

import android.os.Bundle
import ch.bailu.aat.app.AndroidAppContext
import ch.bailu.aat_lib.app.AppContext

abstract class ActivityContext : AbsDispatcher() {
    var appContext: AppContext? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = AndroidAppContext(this, serviceContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        appContext = null
    }
}
