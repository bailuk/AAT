package ch.bailu.aat.services.directory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.simpleio.foc.Foc;

public class GpxDbOpenHelper extends SQLiteOpenHelper {
         private GpxDbOpenHelper(Context context, String name) {
           super(context, name, null, GpxDbConstants.DB_VERSION);
        }

         
         public GpxDbOpenHelper(Context context, Foc file) {
             this(context, file.toString());
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

