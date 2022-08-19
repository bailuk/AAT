package ch.bailu.aat_lib.lib.color;

/**
 * Representation of a color
 */
public interface ColorInterface {

    int GRAY = 0xFF7F7F7F;
    int BLACK = 0xFF000000;
    int MAGENTA = 0xFFFF00FF;
    int WHITE = 0xFFFFFFFF;
    int LTGRAY = 0xFFD3D3D3;
    int DKGRAY = 0xFFD3D3D3;

    int red();
    int green();
    int blue();
    int alpha();

    int toInt();
}
