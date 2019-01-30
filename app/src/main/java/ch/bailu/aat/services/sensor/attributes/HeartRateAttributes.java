package ch.bailu.aat.services.sensor.attributes;

public class HeartRateAttributes extends IndexedAttributes {

    public static final int KEY_INDEX_BPM = 0;
    public static final int KEY_INDEX_RR = 1;
    public static final int KEY_INDEX_CONTACT = 2;
    public static final int KEY_INDEX_LOCATION = 3;


    public static final String[] KEYS = {
            "hr",
            "rr",
            "Contact",
            "Location"
    };


    public static final String[] BODY_SENSOR_LOCATIONS = {
            "Other",
            "Chest",
            "Wrist",
            "Finger",
            "Hand",
            "Ear Lobe",
            "Foot"
    };

    public boolean haveSensorContact = false;

    public int bpm = 0;
    //public int bpmAverage = 0;

    public int rrIntervall = 0;

    public final String location;


    public HeartRateAttributes() {
        this(BODY_SENSOR_LOCATIONS[0]);
    }

    public HeartRateAttributes(String l) {
        super(KEYS);
        location = l;
    }



    @Override
    public String getValue(int index) {
        if (index == KEY_INDEX_BPM) {
            return String.valueOf(bpm);

      //  } else if (index == KEY_INDEX_BPM_AVERAGE) {
      //      return String.valueOf(bpmAverage);

        } else if (index == KEY_INDEX_RR) {
            return String.valueOf(rrIntervall / 1024f);


        } else if (index == KEY_INDEX_CONTACT) {
            if (haveSensorContact) return "";
            return "...";

        } else if (index == KEY_INDEX_LOCATION) {
            return location;

        }

        return NULL_VALUE;
    }

}

