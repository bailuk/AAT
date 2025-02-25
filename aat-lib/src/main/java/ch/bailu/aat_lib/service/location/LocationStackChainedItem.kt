package ch.bailu.aat_lib.service.location

open class LocationStackChainedItem(private val next: LocationStackItem) : LocationStackItem() {
    override fun passLocation(location: LocationInformation) {
        next.passLocation(location)
    }

    override fun passState(state: Int) {
        next.passState(state)
    }
}
