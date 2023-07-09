package ch.bailu.aat.services.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import ch.bailu.aat.util.AppPermission
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.service.location.LocationInformation
import ch.bailu.aat_lib.service.location.LocationStackChainedItem
import ch.bailu.aat_lib.service.location.LocationStackItem
import ch.bailu.aat_lib.util.Objects

@SuppressLint("MissingPermission")
open class RealLocation(i: LocationStackItem?,
                        private val context: Context,
                        private val provider: String,
                        interval: Int

) : LocationStackChainedItem(i), LocationListener {
    private var state = INITIAL_STATE


    init {
        try {
            val lm = locationManager
            validateProvider(lm)
            sendLastKnownLocation(lm)
            requestLocationUpdates(lm, interval.toLong())
            passState(StateID.WAIT)
        } catch (ex: NoServiceException) {
            passState(StateID.NO_SERVICE)
        } catch (ex: SecurityException) {
            passState(StateID.NO_ACCESS)
        } catch (ex: IllegalArgumentException) {
            passState(StateID.NO_ACCESS)
        }
    }

    private fun sendLastKnownLocation(lm: LocationManager) {
        if (AppPermission.checkLocation(context)) {
            val l = lm.getLastKnownLocation(provider)
            if (l != null) passLocation(RealLocationInformation(l, state))
        }
    }

    @Throws(NoServiceException::class)
    @Suppress("DEPRECATION")
    private fun validateProvider(lm: LocationManager) {
        try {
            if (lm.getProvider(provider) == null) {
                throw NoServiceException(provider)
            }
        } catch (e: Exception) {
            throw NoServiceException(provider)
        }
    }

    @get:Throws(NoServiceException::class)
    private val locationManager: LocationManager
        get() = getLocationManager(context)

    @Throws(SecurityException::class, IllegalArgumentException::class)
    private fun requestLocationUpdates(lm: LocationManager, interval: Long) {
        lm.requestLocationUpdates(provider, interval, 0f, this)
    }

    override fun close() {
        try {
            locationManager.removeUpdates(this)
        } catch (e: Exception) {
            state = StateID.NO_SERVICE
        }
    }

    override fun onLocationChanged(l: Location) {
        if (isMine(l)) {
            passState(StateID.ON)
            passLocation(factoryLocationInformation(l, state))
        }
    }

    protected open fun factoryLocationInformation(location: Location, state: Int): LocationInformation {
        return RealLocationInformation(location, state)
    }

    private fun isMine(l: Location?): Boolean {
        return l != null && isMine(l.provider)
    }

    private fun isMine(s: String?): Boolean {
        return Objects.equals(s, provider)
    }

    override fun onProviderDisabled(p: String) {
        if (isMine(p)) {
            passState(StateID.OFF)
        }
    }

    override fun onProviderEnabled(p: String) {
        if (isMine(p)) {
            passState(StateID.WAIT)
        }
    }

    @Suppress("DEPRECATION")
    override fun onStatusChanged(p: String, status: Int, extras: Bundle) {
        if (isMine(p)) {
            if (status == android.location.LocationProvider.AVAILABLE) {
                onProviderEnabled(p)
            } else if (status == android.location.LocationProvider.TEMPORARILY_UNAVAILABLE) {
                onProviderEnabled(p)
            } else {
                onProviderDisabled(p)
            }
        }
    }

    override fun passState(s: Int) {
        if (state != s) {
            state = s
            super.passState(s)
        }
    }

    override fun appendStatusText(builder: StringBuilder) {
        super.appendStatusText(builder)
        builder.append("Provider: ")
        builder.append(provider)
        builder.append("<br>")
        when (state) {
            StateID.NO_ACCESS -> builder.append("STATE_NOACCESS")
            StateID.NO_SERVICE -> builder.append("STATE_NOSERVICE")
            StateID.ON -> builder.append("STATE_ON")
            StateID.OFF -> builder.append("STATE_OFF")
            StateID.PAUSE -> builder.append("STATE_PAUSE")
            StateID.AUTO_PAUSED -> builder.append("STATE_AUTOPAUSED")
            else -> builder.append("STATE_WAIT")
        }
        builder.append("<br>")
    }

    companion object {
        const val INITIAL_STATE = StateID.WAIT

        @Throws(NoServiceException::class)
        fun getLocationManager(context: Context): LocationManager {
            if (AppPermission.checkLocation(context)) {
                val lm = context.getSystemService(Context.LOCATION_SERVICE)
                if (lm is LocationManager) {
                    return lm
                }
            }
            throw NoServiceException("location-manager")
        }

        fun getAllLocationProvidersOrNull(c: Context): List<String>? {
            return try {
                val lm = getLocationManager(c)
                lm.allProviders
            } catch (e: NoServiceException) {
                null
            }
        }
    }
}
