package ch.bailu.aat.gpx;


import android.database.Cursor;

import ch.bailu.aat.services.directory.GpxDbConstants;

public class GpxAttributesDbCursor extends GpxAttributes {
    private final Cursor cursor;
    private final int position;

    public GpxAttributesDbCursor(Cursor c) {
        cursor = c;
        position = cursor.getPosition();
    }


    @Override
    public int size() {
        return GpxDbConstants.KEY_LIST.length;
    }

    @Override
    public void remove(String key) {}


    @Override
    public String getKey(int i) {
        return GpxDbConstants.KEY_LIST[i];
    }

    @Override
    public void put(String key, String value) {}


    @Override
    public String get(String key) {
        for (int p = 0; p < size(); p++) {
            if (getKey(p).equals(key)) {
                return getValue(p);
            }
        }

        for (int p = 0; p < size(); p++) {
            if (getKey(p).contains(key)) {
                return getValue(p);
            }
        }

        return "";
    }


    public String getValue(int i) {
        final int column = cursor.getColumnIndex(getKey(i));

        cursor.moveToPosition(position);
        return cursor.getString(column);
    }
}
