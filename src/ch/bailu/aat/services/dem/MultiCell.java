package ch.bailu.aat.services.dem;

public abstract class MultiCell {

    public abstract void set(final int e);
    public abstract int delta_zx();
    public abstract int delta_zy();
 
    public static MultiCell factory(DemProvider dem) {
        if (dem.inverseLatitude()==true && dem.inverseLongitude()==false) { // NE
            return new MultiCell4NE(dem);

        } else if (dem.inverseLatitude()==false && dem.inverseLongitude()==false) { // SE{
            return new MultiCell4SE(dem);

        } else if (dem.inverseLatitude()==false && dem.inverseLongitude()==true) { // SW{
            return new MultiCell4SW(dem);
            
        } else { // NW
            return new MultiCell4NW(dem);
        }
    }
}
