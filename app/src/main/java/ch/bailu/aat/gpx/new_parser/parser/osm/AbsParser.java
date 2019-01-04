package ch.bailu.aat.gpx.new_parser.parser.osm;

import java.io.IOException;
import java.util.Collections;


import ch.bailu.aat.gpx.new_parser.parser.TagParser;
import ch.bailu.aat.gpx.new_parser.scanner.Scanner;

public abstract class AbsParser extends TagParser {
    public AbsParser(String t) {
        super(t);
    }


    public void havePoint(Scanner scanner) throws IOException {
        if (scanner.tagList.size()>0) {
            Collections.sort(scanner.tagList);
            scanner.wayParsed.onHavePoint();
        }
    }
}
