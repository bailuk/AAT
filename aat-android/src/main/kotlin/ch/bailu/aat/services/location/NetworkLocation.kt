package ch.bailu.aat.services.location

import android.content.Context
import android.location.LocationManager
import ch.bailu.aat_lib.service.location.LocationStackItem

class NetworkLocation(i: LocationStackItem?, context: Context, interval: Int) :
    RealLocation(i, context, LocationManager.NETWORK_PROVIDER, interval)
