package ch.bailu.aat.services.sensor.list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class SensorListDb {

    private SensorListDb() {}


    public static void write(Context context, SensorList list) {
        SQLiteDatabase db = new SensorListDbHelper(context).getWritableDatabase();

        db.delete(SensorListDbContract.TABLE_NAME, null, null);

        for (SensorListItem item : list) {
            if (item.isEnabled())
                write(db, item);
        }
    }


    private static void write(SQLiteDatabase db, SensorListItem item) {

        ContentValues values = new ContentValues();
        values.put(SensorListDbContract.COLUMN_NAME, item.getName());
        values.put(SensorListDbContract.COLUMN_ADDRESS, item.getAddress());


        db.insert(SensorListDbContract.TABLE_NAME, null, values);

    }


    public static void read(Context context, SensorList list) {
        final SQLiteDatabase db = new SensorListDbHelper(context).getReadableDatabase();


        Cursor cursor = db.query(
                SensorListDbContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );



        while(cursor.moveToNext()) {
            final String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(SensorListDbContract.COLUMN_NAME));

            final String address = cursor.getString(
                    cursor.getColumnIndexOrThrow(SensorListDbContract.COLUMN_ADDRESS));

            list.addEnabled(address, name);
        }
        cursor.close();
    }
}
