package ch.bailu.aat.views.graph;

public class Scaler {
    private float scale=1;
    private float real=1;

    
    public Scaler(float scale) {
        if (scale != 0f) this.scale=scale;
    }
    
    
    public Scaler(float scale, float real) {
        this.scale=scale;
        if (real !=0f) this.real=real;
    }
    
    public void init(float real) {
        if (real !=0f) this.real=real;
    }
    
    public float scale(float realValue) {
        return (scale/real)*realValue;
    }
    
    public float backScale(float scaledValue) {
        return scaledValue / (scale/real);
    }
    
    public float getScale() {
        return scale;
    }
    
    public float getReal() {
        return real;
    }
}
