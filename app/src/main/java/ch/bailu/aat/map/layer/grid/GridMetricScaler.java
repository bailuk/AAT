package ch.bailu.aat.map.layer.grid;

public class GridMetricScaler {
    private static final int KM=1000;
    private static final int[] GRID_LEVELS = {
        500*KM,
        200*KM,
        100*KM,
        50*KM,
        20*KM,
        10*KM,
        5*KM,
        2*KM,
        1*KM,
        500,
        200,
        100,
        50,
        20,
        10,
        5,
        2
    };

    private int squareDistance=0;
    
    
    public GridMetricScaler() {}
    
    
    public GridMetricScaler(int distance) {
        findOptimalScale(distance);
    }
    
    
    public int getOptimalScale() {
        return squareDistance;
    }
    
    
    public void findOptimalScale(int distance) {
        for (int level:GRID_LEVELS) {
            if (Math.round(distance / level) > 0) {
                squareDistance=level;
                break;
            }
        }
    }
}
