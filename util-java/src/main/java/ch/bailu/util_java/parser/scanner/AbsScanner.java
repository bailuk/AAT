package ch.bailu.util_java.parser.scanner;

import java.io.IOException;

import ch.bailu.util_java.io.Stream;

public abstract class AbsScanner {

    private final SimpleStringReader stringReader = new SimpleStringReader();
    private final Stream stringStream = new Stream(stringReader);

    public abstract void scan(Stream stream) throws IOException;


    public void scan(String string) throws IOException {
        stringReader.setString(string);
        scan(stringStream);
    }
}
