package ch.bailu.aat_lib.util.color;

import ch.bailu.aat_lib.app.AppColor;
import ch.bailu.aat_lib.util.Limit;

public class AltitudeColorTable {

    private final static int ALTITUDE_OFFSET=500;

    private final static int GRADIENTS=12;
    private final static int GRADIENT_SIZE=256;

    private final static int SIZE = GRADIENTS * GRADIENT_SIZE;
    private final static int MAX=GRADIENT_SIZE-1;

    private final int[] color_table;

    private static final AltitudeColorTable INSTANCE = new AltitudeColorTable();


    public static AltitudeColorTable instance() {
        return INSTANCE;
    }


    private AltitudeColorTable() {
        color_table = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            color_table[i]=calculateColor(i);
        }
    }

    private int calculateColor(int index) {
        int gradient = index / GRADIENT_SIZE;
        int colorIndex = index % GRADIENT_SIZE;

        return calculateColor(gradient, colorIndex);
    }


    private int calculateColor(int gradient, int i) {
        int result;

        switch (gradient) {
            case 0:
                result=rgb(0, i, i);
                break;
            case 1:
            case 7:
                result=rgb(0, MAX-i, MAX);
                break;
            case 2:
            case 8:
                result=rgb(i, 0, MAX);
                break;
            case 3:
            case 9:
                result=rgb(MAX, 0, MAX-i);
                break;
            case 4:
                result=rgb(MAX, i, 0);
                break;
            case 5:
                result=rgb(MAX-i, MAX, 0);
                break;
            case 6:
                result=rgb(0, MAX, i);
                break;
            case 10:
                result=rgb(MAX-i,0, 0);
                break;
            case 11:
                result=rgb(i,i, i);
                break;
            default:
                result= AppColor.HL_ORANGE;
                break;
        }
        return result;
    }

    private int rgb(int r, int g, int b) {
        return new ARGB(r,g,b).toInt();
    }


    public int getColor(int index) {
        return getColorAtIndex(index + ALTITUDE_OFFSET);
    }

    private int getColorAtIndex(int index) {
        index = Limit.clamp(index, 0, color_table.length-1);
        return color_table[index];
    }

}
