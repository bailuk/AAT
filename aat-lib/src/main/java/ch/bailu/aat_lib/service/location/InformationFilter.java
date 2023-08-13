package ch.bailu.aat_lib.service.location;

import javax.annotation.Nonnull;

public final class InformationFilter extends LocationStackChainedItem {

    public InformationFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void passLocation(@Nonnull LocationInformation location) {
        if (location.hasAltitude()) {
            super.passLocation(location);
        }
    }

}
