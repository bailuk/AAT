package ch.bailu.aat.services.directory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GpxDbOpenHelper extends SQLiteOpenHelper {




    public GpxDbOpenHelper(Context context, String path) {
        super(context, path, null, GpxDbConstants.DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(buildCreateExpression());
    }

    private String buildCreateExpression() {
        StringBuilder expression= new StringBuilder();
        expression
                .append("CREATE TABLE ")
                .append(GpxDbConstants.DB_TABLE)
                .append(" (");

        for (int i = 0; i<GpxDbConstants.KEY_LIST_OLD.length; i++) {
            if (i> 0) expression.append(", ");
            expression.append(GpxDbConstants.KEY_LIST_OLD[i]).append(" ").append(GpxDbConstants.TYPE_LIST_OLD[i]);
        }
        expression.append(")");
        return expression.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GpxDbConstants.DB_TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {}
}

