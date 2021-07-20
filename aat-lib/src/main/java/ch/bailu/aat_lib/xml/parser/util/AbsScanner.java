package ch.bailu.aat_lib.xml.parser.util;


import java.io.IOException;

public abstract class AbsScanner {

    private final SimpleStringReader stringReader = new SimpleStringReader();
    private final Stream stringStream = new Stream(stringReader);

    public abstract void scan(Stream stream) throws IOException;


    public void scan(String string) throws IOException {
        stringReader.setString(string);
        scan(stringStream);
    }
}
