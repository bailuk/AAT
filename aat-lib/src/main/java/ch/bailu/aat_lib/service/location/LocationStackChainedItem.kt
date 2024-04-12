package ch.bailu.aat_lib.service.location;

import javax.annotation.Nonnull;

public class LocationStackChainedItem extends LocationStackItem {
    private final LocationStackItem next;

    public LocationStackChainedItem(LocationStackItem n) {
        next = n;
    }

    @Override
    public void passLocation(@Nonnull LocationInformation location) {
        next.passLocation(location);
    }

    @Override
    public void passState(int state) {
        next.passState(state);
    }

}
