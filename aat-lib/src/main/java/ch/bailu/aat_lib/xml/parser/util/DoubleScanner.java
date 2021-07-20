package ch.bailu.aat_lib.xml.parser.util;


import java.io.IOException;

public class DoubleScanner extends AbsScanner {
    private static final int[] exp_table = {
            1,
            10,
            100,
            1000,
            10000,
            100000,
            1000000,
            10000000,
            100000000};
    
    
    private int output=0;
    
    private final int baseExponent;
    
    public DoubleScanner(int be) {
        baseExponent=be;
    }
    
    
    public void setInt(int o) {
    	output = o;
    }
    
    
    public int getInt() {
        return output;
    }

    
    public void scan(Stream stream) throws IOException {
        boolean haveDecimal=false;
        boolean negative=false;

        int exponent=baseExponent;
        int fraction=0;
        
        stream.read();
        stream.skipWhitespace();
        
        if (stream.haveA('-')) {
            negative = true;
            stream.read();
        }
        
        while (true) {
            if (stream.haveDigit()) {
                if (haveDecimal) {
                    if (exponent > 0) {
                        fraction*=10;
                        fraction+=stream.getDigit();
                        exponent--;
                    }
                } else {
                    fraction*=10;
                    fraction+=stream.getDigit();
                }
                
                
                
            } else if (stream.haveA('.')) {
                haveDecimal=true;
                
            } else {
                break;
            }
            stream.read();
        }
        if (negative) fraction = 0-fraction;
        
        output = fraction * exp_table[exponent];//Math.pow(10, exponent);
    }
}
