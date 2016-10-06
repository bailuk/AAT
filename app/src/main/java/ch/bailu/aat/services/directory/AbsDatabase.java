package ch.bailu.aat.services.directory;

import android.database.Cursor;

import java.io.Closeable;

public abstract class AbsDatabase implements Closeable{

    public abstract Cursor query(String selection);


    public abstract void deleteEntry(String pathName);


    public static final AbsDatabase NULL_DATABASE = new AbsDatabase(){


        @Override
        public Cursor query(String selection) {return null;}

        @Override
        public void deleteEntry(String pathName) {}

        @Override
        public void close() {}

    };

    @Override
    public void close() {}
}
