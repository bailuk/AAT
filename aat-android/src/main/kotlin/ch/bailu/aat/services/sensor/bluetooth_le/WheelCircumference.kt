package ch.bailu.aat.services.sensor.bluetooth_le

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.gpx.GpxDeltaHelper
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import java.io.Closeable
import kotlin.math.roundToInt

class WheelCircumference(private val scontext: ServiceContext, private val revolution: Revolution) :
    Closeable {
    private var revolutionsStart: Long = 0
    private var revolutionsDelta: Long = 0
    private var distance = 0f
    private var samples = 0
    private var minSamples = MIN_SAMPLES
    var circumferenceSI = 0f
        private set
    private var currentLocation = GpxInformation.NULL
    private var previousLocation: GpxPointInterface? = null
    private val onLocationChanged: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val newLocation = scontext.getLocationService().getLoggableLocationOrNull(currentLocation)
            if (newLocation != null) {
                currentLocation = newLocation
                if (currentLocation.getAccuracy() <= MIN_ACCURACY && revolution.isInitialized) {
                    if (previousLocation == null) {
                        reset(currentLocation)
                    } else {
                        val dist = GpxDeltaHelper.getDistance(previousLocation, currentLocation)
                        if (dist > MIN_SAMPLE_DISTANCE && dist < MAX_SAMPLE_DISTANCE) {
                            addSample(currentLocation, dist)
                        } else {
                            reset()
                        }
                    }
                } else {
                    reset()
                }
            }
        }
    }

    init {
        AndroidBroadcaster.register(
            scontext.getContext(),
            onLocationChanged,
            AppBroadcaster.LOCATION_CHANGED
        )
    }

    private fun addSample(location: GpxPointInterface, dist: Float) {
        distance += dist
        samples++
        revolutionsDelta = revolution.totalRevolutions - revolutionsStart
        previousLocation = location
        if (samples > minSamples && revolutionsDelta > MIN_REVOLUTIONS) {
            minSamples = samples
            circumferenceSI = distance / revolutionsDelta
        }
    }

    private fun reset(location: GpxPointInterface? = null) {
        previousLocation = location
        distance = 0f
        samples = 0
        revolutionsDelta = 0
        revolutionsStart = revolution.totalRevolutions
    }

    override fun close() {
        scontext.getContext().unregisterReceiver(onLocationChanged)
    }

    // + " D: " + revolutionsDelta;
    val debugString: String
        get() {
            val dist = distance.roundToInt()
            val circ = (circumferenceSI * 100f).roundToInt()
            return "S$samples D$dist C$circ" // + " D: " + revolutionsDelta;
        }

    companion object {
        private const val MIN_SAMPLE_DISTANCE = 0.5f
        private const val MAX_SAMPLE_DISTANCE = 100f
        private const val MIN_ACCURACY = 10f
        private const val MIN_REVOLUTIONS = 10
        private const val MIN_SAMPLES = 5
    }
}
