package ch.bailu.aat_lib.util;

public class Limit {

    public static int inside(int val, int min, int max) {
        val = Math.max(val, min);
        val = Math.min(val, max);
        return val;
    }


    public static int smallest(Integer first, Integer ...ints) {
        int result = first;

        for(Integer i : ints) {
            result = Math.min(first, i);
        }
        return result;
    }

    public static int biggest(Integer first, Integer ...ints) {
        int result = first;

        for(Integer i : ints) {
            result = Math.max(first, i);
        }
        return result;
    }

    public static double smallest(Double first, Double ...ints) {
        double result = first;

        for(Double i : ints) {
            result = Math.min(first, i);
        }
        return result;
    }

    public static double biggest(Double first, Double ...ints) {
        double result = first;

        for(Double i : ints) {
            result = Math.max(first, i);
        }
        return result;
    }

}
