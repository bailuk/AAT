package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.linked_list.Node;

public class GpxListArray {
    private int index=0;
    private final GpxList list;
    private Node current;


    public GpxListArray(GpxList l) {
        list = l;
        current = list.getPointList().getFirst();
    }

    public GpxPointNode get(int i) {
        setIndex(i);
        return get();
    }

    public void setIndex(int i) {
        while (index < i) {
            current= current.getNext();
            index++;
        }

        while (index > i) {
            current = current.getPrevious();
            index--;
        }
    }

    public GpxPointNode get() {
        return (GpxPointNode) current;
    }
    public int size() {
        return list.getPointList().size();
    }
    public GpxList getList() {
        return list;
    }
    public int getIndex() {
        return index;
    }
}
