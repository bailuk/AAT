package ch.bailu.aat.gpx.linked_list;

public class List {
    private Node first=null;
    private Node last=null;
    
    private int count=0;
    
    public void append(Node node) {
        node.setPrevious(last);
        
        if (first==null) {
            first=node;
        } else { 
            last.setNext(node);
        }
        
        last=node;
        count++;
    }
    
    
    public Node getFirst() {
        return first;
    }
    
    
    public Node getLast() {
        return last;
    }
    
    
    public int size() {
        return count;
    }
    
    
}
