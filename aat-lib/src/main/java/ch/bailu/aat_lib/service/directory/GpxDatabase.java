package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.util.sql.Database;
import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.ResultSet;
import ch.bailu.foc.Foc;


public final class GpxDatabase extends AbsDatabase{

    private final String[] keys;
    private final Database database;


    public GpxDatabase (Database database, String path)
            throws DbException {
        this(database, path, GpxDbConstants.KEY_LIST_NEW);
    }

    public GpxDatabase (Database database, String path, String[] k)
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
    public ResultSet query(String selection) {
        return database.query(
                GpxDbConstants.DB_TABLE,
                keys,
                selection,
                null, null, null,
                GpxDbConstants.KEY_START_TIME+ " DESC");
    }


    @Override
    public void close() {
        database.close();
    }

    @Override
    public void deleteEntry(Foc file) throws DbException {
        final String where = GpxDbConstants.KEY_FILENAME + "=?";
        database.delete(GpxDbConstants.DB_TABLE, where,
                        new String[]{file.getName()});
    }

    public void insert(String[] keys, String[] values) {
        database.insert(GpxDbConstants.DB_TABLE, keys, values);
    }
}
