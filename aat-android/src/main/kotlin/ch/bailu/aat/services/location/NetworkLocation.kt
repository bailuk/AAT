package ch.bailu.aat.services.location

import android.content.Context
import android.location.LocationManager
import ch.bailu.aat_lib.service.location.LocationStackItem

class NetworkLocation(next: LocationStackItem, context: Context, interval: Int) :
    RealLocation(next, context, LocationManager.NETWORK_PROVIDER, interval)
