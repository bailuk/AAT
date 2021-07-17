package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.logger.AppLog;

public final class InformationFilter extends LocationStackChainedItem {

    public InformationFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void passLocation(LocationInformation location) {
        if (location.hasAltitude()) {
            AppLog.d(this, "pass location");
            super.passLocation(location);
        } else {
            AppLog.d(this, "do not pass because it has no altitude");
        }
    }

}
