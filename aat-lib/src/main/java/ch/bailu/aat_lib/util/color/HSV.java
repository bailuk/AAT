package ch.bailu.aat_lib.util.color;

public class HSV implements ColorInterface{



    private double H,S,V;

    /**
     * http://www.easyrgb.com/en/math.php#text20
     * @param color
     */
    public HSV(ColorInterface color) {
        //R, G and B input range = 0 ÷ 255
        //H, S and V output range = 0 ÷ 1.0

        double var_R = ( color.red() / 255 );
        double var_G = ( color.green() / 255 );
        double var_B = ( color.blue() / 255 );

        double var_Min = Math.min( var_R, Math.min(var_G, var_B ));
        double var_Max = Math.max( var_R, Math.max(var_G, var_B ));
        double del_Max = var_Max - var_Min;

        V = var_Max;

        H = 0;

        if ( del_Max == 0 ) {
            S = 0;
        } else {
            S = del_Max / var_Max;

            double del_R = ( ( ( var_Max - var_R ) / 6 ) + ( del_Max / 2 ) ) / del_Max;
            double del_G = ( ( ( var_Max - var_G ) / 6 ) + ( del_Max / 2 ) ) / del_Max;
            double del_B = ( ( ( var_Max - var_B ) / 6 ) + ( del_Max / 2 ) ) / del_Max;

            if      ( var_R == var_Max ) H = del_B - del_G;
            else if ( var_G == var_Max ) H = ( 1 / 3 ) + del_R - del_B;
            else if ( var_B == var_Max ) H = ( 2 / 3 ) + del_G - del_R;

            if ( H < 0 ) H += 1;
            if ( H > 1 ) H -= 1;
        }
    }

    /**
     * H, S and V input range = 0 ÷ 1.0
     * R, G and B output range = 0 ÷ 255
     * @return
     */
    @Override
    public int toInt()
    {


        double R,G,B;
        if ( S == 0 )
        {
            R = V * 255;
            G = V * 255;
            B = V * 255;
        }
        else
        {
            double var_h = H * 6;
            if ( var_h == 6 ) var_h = 0;
            double var_i = (int)( var_h );
                double var_1 = V * ( 1 - S );
            double var_2 = V * ( 1 - S * ( var_h - var_i ) );
            double var_3 = V * ( 1 - S * ( 1 - ( var_h - var_i ) ) );

            double var_r, var_g, var_b;

            if      ( var_i == 0 ) { var_r = V     ; var_g = var_3 ; var_b = var_1; }
            else if ( var_i == 1 ) { var_r = var_2 ; var_g = V     ; var_b = var_1; }
            else if ( var_i == 2 ) { var_r = var_1 ; var_g = V     ; var_b = var_3; }
            else if ( var_i == 3 ) { var_r = var_1 ; var_g = var_2 ; var_b = V;     }
            else if ( var_i == 4 ) { var_r = var_3 ; var_g = var_1 ; var_b = V;     }
            else                   { var_r = V     ; var_g = var_1 ; var_b = var_2; }

            R = var_r * 255;
            G = var_g * 255;
            B = var_b * 255;
        }
        return new ARGB((int)R,(int)G,(int)B).toInt();
    }

    @Override
    public int red() {
        return new ARGB(toInt()).red();
    }

    @Override
    public int green() {
        return new ARGB(toInt()).green();
    }

    @Override
    public int blue() {
        return new ARGB(toInt()).blue();
    }

    @Override
    public int alpha() {
        return new ARGB(toInt()).alpha();
    }

    public void setSaturation(float saturation) {
        this.S = saturation;
    }

    public void setValue(float value) {
        this.V = value;
    }
}
