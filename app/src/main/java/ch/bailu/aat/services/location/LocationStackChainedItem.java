package ch.bailu.aat.services.location;


public abstract class LocationStackChainedItem extends LocationStackItem {
    private final LocationStackItem next;
    
    public LocationStackChainedItem(LocationStackItem n) {
        next = n;
    }
    
    @Override
    public void passLocation(LocationInformation l) {
        next.passLocation(l);
    }
    
    
    @Override
    public void passState(int state) {
        next.passState(state);
    }
    
}
