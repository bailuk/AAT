package ch.bailu.aat_lib.service.directory.database;

import java.io.Closeable;

import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;

public abstract class AbsDatabase implements Closeable{

    public abstract DbResultSet query(String selection);


    public abstract void deleteEntry(Foc file) throws DbException;


    public static final AbsDatabase NULL_DATABASE = new AbsDatabase(){


        @Override
        public DbResultSet query(String selection) {return null;}

        @Override
        public void deleteEntry(Foc file) {}

        @Override
        public void close() {}

    };

    @Override
    public void close() {}
}
