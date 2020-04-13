package ch.bailu.aat.services.sensor.attributes;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;

public class HeartRateAttributes extends GpxAttributes {

    private static final Keys KEYS = new Keys();

    public static final int KEY_INDEX_BPM = KEYS.add("HeartRate");
    public static final int KEY_INDEX_RR = KEYS.add("RR");
    public static final int KEY_INDEX_CONTACT = KEYS.add("Contact");
    public static final int KEY_INDEX_LOCATION = KEYS.add("Location");



    public static final String[] BODY_SENSOR_LOCATIONS = {
            "Other",
            "Chest",
            "Wrist",
            "Finger",
            "Hand",
            "Ear Lobe",
            "Foot"
    };

    private static final int BPM_MIN = 30;
    private static final int BPM_MAX = 300;
    private int bpm = 0;

    public boolean haveSensorContact = false;


    public int rrIntervall = 0;

    public final String location;


    public HeartRateAttributes() {
        this(BODY_SENSOR_LOCATIONS[0]);
    }

    public HeartRateAttributes(String l) {
        location = l;
    }


    public void setBpm(int bpm) {
        if (bpm > BPM_MIN && bpm < BPM_MAX) {
            this.bpm = bpm;
        } else {
            this.bpm = 0;
        }
    }


    public boolean haveBpm() {
        return bpm > 0;
    }

    @Override
    public String get(int keyIndex) {
        if (keyIndex == KEY_INDEX_BPM) {
            return String.valueOf(bpm);

      //  } else if (keyIndex == KEY_INDEX_BPM_AVERAGE) {
      //      return String.valueOf(bpmAverage);

        } else if (keyIndex == KEY_INDEX_RR) {
            return String.valueOf(rrIntervall / 1024f);


        } else if (keyIndex == KEY_INDEX_CONTACT) {
            if (haveSensorContact) return "";
            return "...";

        } else if (keyIndex == KEY_INDEX_LOCATION) {
            return location;

        }

        return NULL_VALUE;
    }

    @Override
    public boolean hasKey(int keyIndex) {
        return KEYS.hasKey(keyIndex);
    }

    @Override
    public int size() {
        return KEYS.size();
    }

    @Override
    public String getAt(int i) {
        return get(KEYS.getKeyIndex(i));
    }

    @Override
    public int getKeyAt(int i) {
        return KEYS.getKeyIndex(i);
    }

}

