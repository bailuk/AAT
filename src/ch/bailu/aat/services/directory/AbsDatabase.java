package ch.bailu.aat.services.directory;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbsDatabase implements Closeable{
    public abstract AbsIterator getIterator();

    public abstract void reopenCursor();
    public abstract void reopenCursor(String selection);

    public abstract void reopenDatabase() throws IOException;
    public abstract void reopenDatabase(String selection) throws IOException;

    public abstract void deleteEntry(String pathName);


    public static final AbsDatabase NULL_DATABASE = new AbsDatabase(){

        @Override
        public AbsIterator getIterator() {
            return AbsIterator.NULL_ITERATOR;
        }

        @Override
        public void reopenCursor(String selection) {}

        @Override
        public void reopenDatabase(String selection) throws IOException {}

       

        @Override
        public void reopenCursor() {}

        @Override
        public void reopenDatabase() throws IOException {}

        @Override
        public void deleteEntry(String pathName) {}

        @Override
        public void close() {
        }

    };

    @Override
    public void close() {}
}
