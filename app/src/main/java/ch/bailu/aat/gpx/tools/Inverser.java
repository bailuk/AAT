package ch.bailu.aat.gpx.tools;

import ch.bailu.aat.gpx.AltitudeDelta;
import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListArray;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointFirstNode;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.MaxSpeed;

public class Inverser {


    private final GpxList newList;
    private final GpxListArray listInverse;


    public Inverser(GpxList track) {
        newList = new GpxList(track.getDelta().getType(),
                MaxSpeed.NULL,  AutoPause.NULL, AltitudeDelta.NULL);


        GpxListArray list = new GpxListArray(track);
        listInverse = new GpxListArray(track);

        if (list.size() > 0) {

            int indexInverse = list.size() -1;
            int index = 0;

            while (indexInverse >=0) {
                listInverse.setIndex(indexInverse);
                list.setIndex(index);

                final GpxPointNode point = list.get();
                final GpxPointNode pointInverse = listInverse.get();

                final GpxPoint pointNew = new GpxPoint(pointInverse, pointInverse.getAltitude(),
                        point.getTimeStamp());


                if (isLastInSegment(pointInverse))
                    newList.appendToNewSegment(pointNew, pointInverse.getAttributes());
                else
                    newList.appendToCurrentSegment(pointNew, pointInverse.getAttributes());


                index++;
                indexInverse--;
            }
        }

    }


    private boolean isLastInSegment(GpxPointNode point) {
        return point.getNext() == null || point.getNext() instanceof GpxPointFirstNode;
    }

    public GpxList getNewList() {
        return newList;
    }
}
