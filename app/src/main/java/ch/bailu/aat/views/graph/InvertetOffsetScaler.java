package ch.bailu.aat.views.graph;


public class InvertetOffsetScaler {

    private Scaler scaler;
    private float min=Float.MAX_VALUE,max=0;
    public InvertetOffsetScaler(int pixel) {
        scaler=new Scaler(pixel);
    }

    public void addValue(float v) {
        min = Math.min(min,v);
        max = Math.max(max,v);
        scaler.init(max-min);
    }
    
    public float scale(float v) {
        return scaler.getScale()-(scaler.scale(v-min));
    }
    
    public float getRealDistance() {
        return max-min;
    }
    public float getRealOffset() {
        return min;
    }
    
    public float getRealTop() {
        return max;
    }
    
    public void round(int roundTo) {
        double 
        d = max/roundTo;
        d = Math.floor(d);
        max = (float)(d*roundTo)+roundTo;
        
        d = min/roundTo;
        d = Math.floor(d);
        min = (float)d*roundTo;
        
        addValue(max);
        addValue(min);
    }
    
}
