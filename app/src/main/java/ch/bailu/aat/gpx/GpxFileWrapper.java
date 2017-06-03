package ch.bailu.aat.gpx;

import ch.bailu.simpleio.foc.Foc;


public class GpxFileWrapper extends GpxInformation {
    private final GpxList list;
    private final Foc file;


    public GpxFileWrapper(Foc f, GpxList l) {
        list = l;
        file = f;

        if (list.getPointList().size()>0)
            setVisibleTrackPoint((GpxPointNode)list.getPointList().getLast());

        this.setVisibleTrackSegment(list.getDelta());
    }

    @Override
    public boolean isLoaded() {
        return true;
    }


    @Override
    public GpxList getGpxList() {
        return list;
    }
    
    @Override
    public Foc getFile() {
        return file;
    }
}
