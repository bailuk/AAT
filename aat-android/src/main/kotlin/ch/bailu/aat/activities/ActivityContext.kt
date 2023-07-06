package ch.bailu.aat.activities

import android.os.Bundle
import ch.bailu.aat.app.AndroidAppContext
import ch.bailu.aat_lib.app.AppContext

abstract class ActivityContext : AbsDispatcher() {
    private var _appContext: AppContext? = null

    val appContext: AppContext
        get() = _appContext!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _appContext = AndroidAppContext(this, serviceContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        _appContext = null
    }
}
