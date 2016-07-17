package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.activities.AbsServiceLink;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.FileAction;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.IteratorFollowFile;
import ch.bailu.aat.services.directory.IteratorSummary;

public abstract class IteratorSource extends ContentSource {

    
    private final ServiceContext scontext;
    private Iterator iterator = Iterator.NULL;

    private final SolidDirectory  sdirectory;



    private final BroadcastReceiver  onChangedInCache = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppBroadcaster.hasFile(intent, iterator.getInfo().getPath())) {
                forceUpdate();
            }
        }
    };


    public IteratorSource(ServiceContext sc) {
        scontext = sc;
        sdirectory = new SolidDirectory(sc.getContext());
    }

    @Override
    public void forceUpdate() {
        if (iterator.getInfo().isLoaded()) {
            updateGpxContent(iterator.getInfo());
        }
    }


    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onChangedInCache);

        iterator.close();
        iterator = Iterator.NULL;
    }


    @Override
    public void onResume() {
        String selection = new SolidDirectory(scontext.getContext()).getSelection();
        iterator = factoryIterator(scontext, selection);
        iterator.moveToPosition(sdirectory.getPosition());

        AppBroadcaster.register(scontext.getContext(), onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    public abstract Iterator factoryIterator(ServiceContext scontext, String selection);

    public GpxInformation getInfo() {
        return iterator.getInfo();
    }
    
    public Iterator getIterator() {
        return iterator;
    }

    public void moveToPrevious() {
        if (iterator.moveToPrevious() == false) 
            iterator.moveToPosition(iterator.getCount()-1);
        
        sdirectory.setPosition(iterator.getPosition());
        forceUpdate();
    }

    public void moveToNext() {
        if (iterator.moveToNext() == false) 
            iterator.moveToPosition(0);
        
        sdirectory.setPosition(iterator.getPosition());
        forceUpdate();
    }

    public FileAction fileAction(AbsServiceLink activity) {
        return new FileAction(activity, iterator);
    }
    
    
    
    public static class FollowFile extends IteratorSource {
        public FollowFile(ServiceContext sc) {
            super(sc);
        }

        @Override
        public Iterator factoryIterator(ServiceContext scontext,
                String selection) {
            return new IteratorFollowFile(scontext, selection);
        }
        
    }
    
    
    
    public static class Summary extends IteratorSource {
        public Summary(ServiceContext sc) {
            super(sc);
        }

        @Override
        public Iterator factoryIterator(ServiceContext scontext,
                String selection) {
            return new IteratorSummary(scontext, selection);
        }
        
    }

}
