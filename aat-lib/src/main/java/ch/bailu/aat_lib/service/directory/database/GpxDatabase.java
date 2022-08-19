package ch.bailu.aat_lib.service.directory.database;

import ch.bailu.aat_lib.util.sql.DbConnection;
import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;


public final class GpxDatabase extends AbsDatabase {
    private final String[] keys;
    private final DbConnection database;


    public GpxDatabase (DbConnection database, String path)
            throws DbException {
        this(database, path, GpxDbConfiguration.KEY_LIST);
    }

    public GpxDatabase (DbConnection database, String path, String[] keys)
            throws DbException {

        this.keys = keys;
        this.database = database;
        this.database.open(path, GpxDbConfiguration.DB_VERSION);
    }

    @Override
    public DbResultSet query(String selection) {
        return database.query("SELECT " + join(keys) + " FROM " + GpxDbConfiguration.TABLE + where(selection) + " ORDER BY " + GpxDbConfiguration.KEY_START_TIME + " DESC");
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
        database.execSQL("DELETE FROM " + GpxDbConfiguration.TABLE + " WHERE "+ GpxDbConfiguration.KEY_FILENAME+ " = ?", file.getName());
    }

    public void insert(String[] keys, Object ... values) {
        database.execSQL("INSERT INTO " + GpxDbConfiguration.TABLE + " ( " + join(keys) + ") VALUES ( " + repeat("?" , values.length) + " )", values);
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
