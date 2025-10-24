package ch.bailu.aat_lib.util;

public class Limit {

    public static boolean isInRange(int val, int min, int max) {
        return clamp(val, min, max) == val;
    }

    public static boolean isBetween(int val, int min, int max) {
        return clamp(val, min+1, max-1) == val;
    }

    public static int clamp(int val, int min, int max) {
        val = Math.max(val, min);
        val = Math.min(val, max);
        return val;
    }

    public static int smallest(Integer value, Integer ...values) {
        for(Integer v : values) {
            value = Math.min(value, v);
        }
        return value;
    }

    public static int biggest(Integer value, Integer ...values) {
        for(Integer val : values) {
            value = Math.max(value, val);
        }
        return value;
    }

    public static double smallest(Double value, Double ...values) {
        for(Double v : values) {
            value = Math.min(value, v);
        }
        return value;
    }

    public static double biggest(Double value, Double ...values) {
        for(Double v : values) {
            value = Math.max(value, v);
        }
        return value;
    }

}
