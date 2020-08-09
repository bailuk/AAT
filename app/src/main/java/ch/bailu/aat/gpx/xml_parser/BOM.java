package ch.bailu.aat.gpx.xml_parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import ch.bailu.foc.Foc;

public class BOM {
    public static final int BOM_UTF8    = 0xEFBBBF;
    public static final int BOM_UTF16BE = 0xFEFF;
    public static final int BOM_UTF16LE = 0xFFFE;
    public static final int BOM_UTF32BE = 0x0000FEFF;
    public static final int BOM_UTF32LE = 0xFFFE0000;


    public static boolean hasBOM(Reader reader) throws IOException {
        final int x = reader.read();

        return ((x > -1) && isBOM(x));
    }

    private static boolean isBOM(int in) {
        return  in == BOM_UTF8    ||
                in == BOM_UTF16BE ||
                in == BOM_UTF16LE ||
                in == BOM_UTF32BE ||
                in == BOM_UTF32LE;
    }


    public static Reader open(Foc file) throws IOException {
        Reader reader = new InputStreamReader(file.openR());

        if (!hasBOM(reader)) {
            reader.close();
            reader = new InputStreamReader(file.openR());

        }
        return new BufferedReader(reader);
    }
}
