package ch.bailu.aat_lib.service.location;

public final class InformationFilter extends LocationStackChainedItem {

    public InformationFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void passLocation(LocationInformation location) {
        if (location.hasAltitude()) {
            super.passLocation(location);
        }
    }

}
