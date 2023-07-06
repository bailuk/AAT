package ch.bailu.aat.activities

import android.app.Activity
import android.os.Bundle
import ch.bailu.aat.preferences.PreferenceLoadDefaults
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.AppPermission
import ch.bailu.aat.views.msg.ErrorMsgView
import ch.bailu.aat_lib.preferences.system.SolidStartCount

abstract class AbsActivity : Activity() {
    protected var errorView: ErrorMsgView? = null
        private set

    init {
        instantiated++
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        errorView = ErrorMsgView(this)
        errorView?.registerReceiver()

        PreferenceLoadDefaults(this)
        created++
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        AppPermission.onRequestPermissionsResult(this, requestCode)
    }

    public override fun onDestroy() {
        errorView?.unregisterReceiver()
        created--
        super.onDestroy()
    }

    public override fun onPause() {
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        instantiated--
    }

    fun appendStatusText(builder: StringBuilder) {
        builder.append("<h1>")
        builder.append(javaClass.simpleName)
        builder.append("</h1>")
        builder.append("<p>Instantiated activities: ")
        builder.append(instantiated)
        builder.append("<br>Created activities: ")
        builder.append(created)
        builder.append("<br>Count of application starts: ")
        builder.append(SolidStartCount(Storage(this)).value)
        builder.append("</p>")
    }

    companion object {
        private var instantiated = 0
        private var created = 0
    }
}
