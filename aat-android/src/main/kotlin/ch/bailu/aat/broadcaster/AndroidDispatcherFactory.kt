package ch.bailu.aat.broadcaster

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.source.FixedOverlaySource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface

fun DispatcherInterface.addMapOverlaySources(appContext: AppContext, usageTrackers: UsageTrackerInterface) {
    this.addOverlaySources(appContext, usageTrackers)
    this.addSource(FixedOverlaySource.createDraftSource(appContext, usageTrackers))
    this.addSource(FixedOverlaySource.createPoiSource(appContext, usageTrackers))
    this.addSource(FixedOverlaySource.createBrouterSource(appContext, usageTrackers))
    this.addSource(FixedOverlaySource.createNominatimReverseSource(appContext, usageTrackers))
    this.addSource(FixedOverlaySource.createNominatimSource(appContext, usageTrackers))
    this.addSource(FixedOverlaySource.createOverpassSource(appContext, usageTrackers))
}
