package ch.bailu.aat.gpx;

import java.io.File;


public class GpxFileWrapper extends GpxInformation {
    private final GpxList list;
    private final File    file;


    public GpxFileWrapper(File f, GpxList l) {
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
    public String getName() {
        return file.getName();
    }
    
    @Override
    public String getPath() {
        return file.getPath();
    }
}
