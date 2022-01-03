package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.util.sql.DbConnection;
import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;


public final class GpxDatabase extends AbsDatabase{

    private final String[] keys;
    private final DbConnection database;


    public GpxDatabase (DbConnection database, String path)
            throws DbException {
        this(database, path, GpxDbConstants.KEY_LIST_NEW);
    }

    public GpxDatabase (DbConnection database, String path, String[] k)
            throws DbException {

        keys = k;
        this.database = database;
        database.open(path, GpxDbConstants.DB_VERSION, (db) -> {
            db.execSQL("DROP TABLE IF EXISTS " + GpxDbConstants.DB_TABLE);
            db.execSQL(getCreateTableExpression());
        });
    }

    private String getCreateTableExpression() {
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
    public DbResultSet query(String selection) {
        return database.query("SELECT " + join(keys) + " FROM " + GpxDbConstants.DB_TABLE + where(selection) + " ORDER BY " + GpxDbConstants.KEY_START_TIME + " DESC");
    }

    private static String where(String selection) {
        if (selection != null && selection.length() > 3) {
            return " WHERE " + selection;
        }
        return "";
    }

    @Override
    public void close() {
        database.close();
    }

    @Override
    public void deleteEntry(Foc file) throws DbException {
        database.execSQL("DELETE FROM " + GpxDbConstants.DB_TABLE + " WHERE "+ GpxDbConstants.KEY_FILENAME+ " = ?", file.getName());
    }

    public void insert(String[] keys, Object ... values) {
        database.execSQL("INSERT INTO " + GpxDbConstants.DB_TABLE + " ( " + join(keys) + ") VALUES ( " + repeat("?" , values.length) + " )", values);
    }

    private static String repeat(String what, int times) {
        StringBuilder builder = new StringBuilder();
        String del = "";
        for (int i = 0; i<times; i++) {
            builder.append(del);
            builder.append(what);
            del = ", ";
        }
        return builder.toString();
    }

    private static String join(String[] keys) {
        StringBuilder builder = new StringBuilder();
        String del = "";
        for (String k : keys) {
            builder.append(del);
            builder.append(k);
            del = ", ";
        }
        return builder.toString();
    }
}
