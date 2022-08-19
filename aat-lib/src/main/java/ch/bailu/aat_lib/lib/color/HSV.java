package ch.bailu.aat_lib.lib.color;

import ch.bailu.aat_lib.util.Limit;

public class HSV implements ColorInterface{


    private int alpha;
    private double H,S,V;

    /**
     * http://www.easyrgb.com/en/math.php#text20
     * @param color a color
     */
    public HSV(ColorInterface color) {
        //R, G and B input range = 0 ÷ 255
        //H, S and V output range = 0 ÷ 1.0

        alpha = color.alpha();

        double var_R = ( ((double)color.red()) / 255d );
        double var_G = ( ((double)color.green()) / 255d );
        double var_B = ( ((double)color.blue()) / 255d );

        double var_Min = Limit.smallest(var_R, var_G, var_B);
        double var_Max = Limit.biggest( var_R, var_G, var_B );
        double del_Max = var_Max - var_Min;

        V = var_Max;

        H = 0d;

        if ( del_Max == 0d ) {
            S = 0d;
        } else {
            S = del_Max / var_Max;

            double del_R = ( ( ( var_Max - var_R ) / 6d ) + ( del_Max / 2d ) ) / del_Max;
            double del_G = ( ( ( var_Max - var_G ) / 6d ) + ( del_Max / 2d ) ) / del_Max;
            double del_B = ( ( ( var_Max - var_B ) / 6d ) + ( del_Max / 2d ) ) / del_Max;

            if      ( var_R == var_Max ) H = del_B - del_G;
            else if ( var_G == var_Max ) H = ( 1d / 3d ) + del_R - del_B;
            else if ( var_B == var_Max ) H = ( 2d / 3d ) + del_G - del_R;

            if ( H < 0d ) H += 1d;
            if ( H > 1d ) H -= 1d;
        }
    }

    /**
     * H, S and V input range = 0 ÷ 1.0
     * R, G and B output range = 0 ÷ 255
     * @return return argb color as integer
     */
    @Override
    public int toInt()
    {
        double R,G,B;
        if ( S == 0 )
        {
            R = V * 255d;
            G = V * 255d;
            B = V * 255d;
        }
        else
        {
            double var_h = H * 6d;
            if ( var_h == 6d ) var_h = 0d;
            int var_i = (int)( var_h );
            double var_1 = V * ( 1d - S );
            double var_2 = V * ( 1d - S * ( var_h - var_i ) );
            double var_3 = V * ( 1d - S * ( 1d - ( var_h - var_i ) ) );

            double var_r, var_g, var_b;

            if      ( var_i == 0 ) { var_r = V     ; var_g = var_3 ; var_b = var_1; }
            else if ( var_i == 1 ) { var_r = var_2 ; var_g = V     ; var_b = var_1; }
            else if ( var_i == 2 ) { var_r = var_1 ; var_g = V     ; var_b = var_3; }
            else if ( var_i == 3 ) { var_r = var_1 ; var_g = var_2 ; var_b = V;     }
            else if ( var_i == 4 ) { var_r = var_3 ; var_g = var_1 ; var_b = V;     }
            else                   { var_r = V     ; var_g = var_1 ; var_b = var_2; }

            R = var_r * 255d;
            G = var_g * 255d;
            B = var_b * 255d;
        }
        return new ARGB(alpha, (int)R,(int)G,(int)B).toInt();
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
        return alpha;
    }

    public void setSaturation(float saturation) {
        this.S = saturation;
    }

    public void setValue(float value) {
        this.V = value;
    }
}
