package ch.bailu.aat_lib.gpx.linked_list;

public class Node {
    public final static long SIZE_IN_BYTES=16;


    private Node previous=null;
    private Node next=null;


    protected void setPrevious(Node node) {
        previous=node;
    }


    protected void setNext(Node node) {
        next=node;
    }


    public Node getNext() {
        return next;
    }


    public Node getPrevious() {
        return previous;
    }

}
