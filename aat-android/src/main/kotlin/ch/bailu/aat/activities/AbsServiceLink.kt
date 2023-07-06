package ch.bailu.aat.activities

import android.os.Bundle
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.ServiceLink

abstract class AbsServiceLink : AbsHardwareButtons() {
    private var serviceLink: ServiceLink? = null

    val serviceContext: ServiceContext
        get() = serviceLink!!


    private enum class State {
        Destroyed, Created, Resumed, ServiceUp
    }

    private var state = State.Destroyed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = State.Created
        serviceLink = object : ServiceLink(this) {
            override fun onServiceUp() {
                if (state == State.Resumed) {
                    onResumeWithService()
                    state = State.ServiceUp
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        state = State.Resumed
        serviceLink?.up()
    }

    override fun onPause() {
        if (state == State.ServiceUp) {
            onPauseWithService()
        }
        serviceLink?.down()
        state = State.Created
        super.onPause()
    }

    open fun onResumeWithService() {}
    open fun onPauseWithService() {}
    override fun onDestroy() {
        serviceLink?.close()
        serviceLink = null
        state = State.Destroyed
        super.onDestroy()
    }
}
