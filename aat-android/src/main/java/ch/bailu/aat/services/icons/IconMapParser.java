package ch.bailu.aat.services.icons;

import java.io.IOException;

import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.foc.Foc;
import ch.bailu.util_java.io.Stream;


public final class IconMapParser {
    private final static int ICON=0, KEY=1, VALUE=2, END=3, MAX=4;

    private final String[] entries = new String[MAX];
    private int entry=0;

    private final StringBuilder buffer = new StringBuilder();

    IconMapParser(Foc file, IconMap map) throws IOException {
        Stream stream = new Stream(file);

        stream.read();

        while(true) {
            if (stream.haveA('#')) stream.to('\n');

            if (stream.haveA('\n')) {
                addEntry(map);
                stream.read();

            } else if (stream.haveCharacter()) {
                parseSubEntry(stream);

            } else if (stream.haveEOF()){
                break;

            } else {
                stream.read();
            }
        }
    }

    private void parseSubEntry(Stream stream) throws IOException {
        buffer.setLength(0);

        while(
                stream.haveA('_') ||
                stream.haveA('/') ||
                stream.haveCharacter() ||
                stream.haveDigit())
        {
            buffer.append((char)stream.get());
            stream.read();
        }

        entries[entry]=buffer.toString();

        if (entry < END) {
            entry++;
        }
    }

    private void addEntry(IconMap map) {
        if (entry==END) {
            map.add(Keys.toIndex(entries[KEY]), entries[VALUE], entries[ICON]);
        }
        entry=0;

    }
}
