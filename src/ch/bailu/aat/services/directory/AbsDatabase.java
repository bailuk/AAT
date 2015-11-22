package ch.bailu.aat.services.directory;

import java.io.IOException;

import ch.bailu.aat.helpers.CleanUp;

public abstract class AbsDatabase implements CleanUp{
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
        public void cleanUp() {}

        @Override
        public void reopenCursor() {}

        @Override
        public void reopenDatabase() throws IOException {}

        @Override
        public void deleteEntry(String pathName) {}

    };

}
