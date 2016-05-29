package ch.bailu.aat.services;

import android.content.Context;

public class VirtualServiceLink extends ServiceContext {

    private final  OneService service;
    
    public VirtualServiceLink(OneService s) {
        service = s;
    }
    @Override
    public Context getContext() {
        return service;
    }

    @Override
    public OneService getService() throws ServiceNotUpException {
       return service;
        
    }

    @Override
    public void lock(String s) {
        service.lock(s);
    }
    
    @Override
    public void free(String s) {
        service.free(s);
    }


}
