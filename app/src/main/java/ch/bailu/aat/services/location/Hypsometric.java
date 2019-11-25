package ch.bailu.aat.services.location;

public final class Hypsometric {
    /**
     *  See
     *  https://physics.stackexchange.com/questions/333475/how-to-calculate-altitude-from-current-temperature-and-pressure
     */

    private final static double MIN_PRESSURE=100d;

    private final static double TEMPERATURE_CELSIUS = 15d;
    private final static double TEMPERATURE_KELVIN = TEMPERATURE_CELSIUS + 273.15d;
    private final static double Ex = 5.257d;
    private final static double Rx = 1d / Ex;
    private final static double Ax = 0.0065d;



    private double pressureAtSeaLevel = 0;
    private double pressure = 0;


    public void setPressure(double d) {
        pressure = d;
    }


    public void setAltitude(double a) {
        if (isPressureValid()) {
            pressureAtSeaLevel = getPressureAtSeaLevel(pressure, a);
        }
    }


    public double getAltitude() {
        if (isValid()) {
            return  (getAltitude(pressure, pressureAtSeaLevel));
        }

        return 0;
    }


    public double getPressureAtSeaLevel() {
        return pressureAtSeaLevel;
    }


    public boolean isPressureValid() {
        return pressure > MIN_PRESSURE;
    }

    public boolean isPressureAtSeaLevelValid() {
        return pressureAtSeaLevel > MIN_PRESSURE;
    }


    public boolean isValid() {
        return isPressureValid() && isPressureAtSeaLevelValid();
    }




    public static double getAltitude(double pressure, double pressureAtSeaLevel) {

        final double Px = pressureAtSeaLevel / pressure;

        return ((Math.pow(Px, Rx) -1d) * TEMPERATURE_KELVIN)/Ax;
    }


    public static double getPressureAtSeaLevel(double pressure, double altitude) {

        final double Hx = altitude * Ax;

        return pressure * Math.pow( (Hx / TEMPERATURE_KELVIN) + 1d, Ex);
    }

    public void setPressureAtSeaLevel(double pressure) {
        pressureAtSeaLevel = pressure;
    }
}
