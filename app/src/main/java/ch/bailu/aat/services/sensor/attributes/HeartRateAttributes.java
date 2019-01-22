package ch.bailu.aat.services.sensor.attributes;

import ch.bailu.aat.gpx.GpxAttributes;

public class HeartRateAttributes extends GpxAttributes {

    public static final int KEY_INDEX_BPM = 0;
    public static final int KEY_INDEX_BPM_AVERAGE = 1;
    public static final int KEY_INDEX_RR = 2;
    public static final int KEY_INDEX_CONTACT = 3;
    public static final int KEY_INDEX_LOCATION = 3;


    public static final String[] KEYS = {
            "BPM",
            "AverageBPM",
            "RR",
            "ContactStatus",
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
    public int bpmAverage = 0;

    public int rrIntervall = 0;

    public final String location;


    public HeartRateAttributes() {
        this(BODY_SENSOR_LOCATIONS[0]);
    }

    public HeartRateAttributes(String l) {
        location = l;
    }


    @Override
    public String get(String key) {
        for (int i = 0; i< KEYS.length; i++) {
            if (key.equalsIgnoreCase(KEYS[i])) return getValue(i);
        }

        return null;
    }

    @Override
    public String getValue(int index) {
        if (index == KEY_INDEX_BPM) {
            return String.valueOf(bpm);

        } else if (index == KEY_INDEX_BPM_AVERAGE) {
            return String.valueOf(bpmAverage);

        } else if (index == KEY_INDEX_RR) {
            return String.valueOf(rrIntervall / 1024f);


        } else if (index == KEY_INDEX_CONTACT) {
            if (haveSensorContact) return "on";
            return "...";

        } else if (index == KEY_INDEX_LOCATION) {
            return location;

        }

        return NULL_VALUE;
    }

    @Override
    public String getKey(int index) {
        if (index < KEYS.length) return KEYS[index];
        return null;
    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public int size() {
        return KEYS.length;
    }

    @Override
    public void remove(String key) {

    }
}

