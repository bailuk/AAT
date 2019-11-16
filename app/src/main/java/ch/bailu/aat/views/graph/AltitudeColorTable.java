package ch.bailu.aat.views.graph;

import android.graphics.Color;

public class AltitudeColorTable extends ColorTable{

    private final static int ALTITUDE_OFFSET=500;
    private final static int MAX_ALTITUDE=3000;
    private final static int GRADIENTS=12;

    public static final ColorTable INSTANCE = new AltitudeColorTable();


    public AltitudeColorTable() {
        super(MAX_ALTITUDE, GRADIENTS);
    }

    @Override
    public int getColor(int index) {
        return super.getColor(index + ALTITUDE_OFFSET);
    }

    @Override
    public int calculateColor(int gradient, int i) {
        int color;

        switch (gradient) {
        case 0:
            color=Color.rgb(0, i, i);
            break;
        case 1:
            color=Color.rgb(0, MAX-i, MAX);
            break;
        case 2:
            color=Color.rgb(i, 0, MAX);
            break;
        case 3:
            color=Color.rgb(MAX, 0, MAX-i);
            break;
        case 4:
            color=Color.rgb(MAX, i, 0);
            break;
        case 5:
            color=Color.rgb(MAX-i, MAX, 0);
            break;
        case 6:
            color=Color.rgb(0, MAX, i);
            break;
        case 7:
            color=Color.rgb(0, MAX-i, MAX);
            break;
        case 8:
            color=Color.rgb(i, 0,MAX);
            break;
        case 9:
            color=Color.rgb(MAX,0, MAX-i);
            break;
        case 10:
            color=Color.rgb(MAX-i,0, 0);
            break;
        case 11:
            color=Color.rgb(i,i, i);
            break;
        default:
            color=super.calculateColor(gradient, i);
            break;
        }
        return color;
    }
}
