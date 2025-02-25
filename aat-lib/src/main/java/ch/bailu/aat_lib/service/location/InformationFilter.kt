package ch.bailu.aat_lib.service.location

class InformationFilter(next: LocationStackItem) : LocationStackChainedItem(next) {
    override fun passLocation(location: LocationInformation) {
        if (location.hasAltitude()) {
            super.passLocation(location)
        }
    }
}
