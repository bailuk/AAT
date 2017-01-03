package ch.bailu.aat.views.graph;

import ch.bailu.aat.util.ui.AppTheme;

public class ColorTable {
    public final static int GRADIENT_SIZE=256;
    public final static int MAX=GRADIENT_SIZE-1;
    
    private final int[] color_table;

    public static final ColorTable altitude = new AltitudeColorTable();
    
    public ColorTable(int spectrum, int gradients) {
        Scaler scaler=new Scaler(gradients*GRADIENT_SIZE, spectrum);
        color_table = new int[spectrum];
        
        for (int i=0; i < spectrum; i++) {
            color_table[i]=calculateColor((int)scaler.scale(i));
        }
    }

    
    private int calculateColor(int index) {
        int gradient=index/GRADIENT_SIZE;
        index=index-(GRADIENT_SIZE*gradient);
        
        return calculateColor(gradient, index);
    }
    
    public int calculateColor(int gradient, int i) {
        return AppTheme.getHighlightColor();
    }
        
    public int getColor(int index) {
        index = Math.max(0, index);
        index = Math.min(color_table.length-1, index);
        return color_table[index];
    }
    
    

}
