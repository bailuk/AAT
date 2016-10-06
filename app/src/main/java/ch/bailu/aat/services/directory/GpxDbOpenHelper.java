package ch.bailu.aat.services.directory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class GpxDbOpenHelper extends SQLiteOpenHelper {
         public GpxDbOpenHelper(Context context, String name) {
           super(context, name, null, GpxDbConstants.DB_VERSION);
        }

         
         public GpxDbOpenHelper(Context context, File file) {
             this(context, file.getAbsolutePath());
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
            
            for (int i=0; i<GpxDbConstants.KEY_LIST.length; i++) {
                if (i> 0) expression.append(", ");
                expression.append(GpxDbConstants.KEY_LIST[i]).append(" ").append(GpxDbConstants.TYPE_LIST[i]);
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

