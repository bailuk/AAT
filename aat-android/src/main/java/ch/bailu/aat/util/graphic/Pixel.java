package ch.bailu.aat.util.graphic;

import android.graphics.Point;

public class Pixel extends Point {


    public Pixel(int x, int y) {
        super(x,y);
    }

    public Pixel() {
        super();
    }

    public void setCopy(Pixel pixel) {
        this.x = pixel.x;
        this.y = pixel.y;
    }
}
