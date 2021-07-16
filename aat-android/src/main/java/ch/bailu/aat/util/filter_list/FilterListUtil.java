package ch.bailu.aat.util.filter_list;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


import ch.bailu.foc.Foc;

public class FilterListUtil {
    private final static int LINE_LIMIT = 500;
    private final static int LINE_EST = 50;


    private final FilterList list;


    public FilterListUtil(FilterList l) {
        list = l;
    }


    public void writeSelected(Foc file) throws IOException {

        try (Writer out = new OutputStreamWriter(new BufferedOutputStream(file.openW()))) {

            for (int e = 0; e < list.sizeVisible(); e++) {
                if (list.getFromVisible(e).isSelected()) {

                    KeyList keys = list.getFromVisible(e).getKeys();

                    for (int k = 0; k < keys.size() - 1; k++) {
                        out.write(keys.getKey(k));
                        out.write(' ');
                    }

                    out.write('\n');
                }
            }
        }
    }


    public void readSelected(Foc file) throws IOException {

        try (Reader in = new InputStreamReader(new BufferedInputStream(file.openR()))) {
            final StringBuilder builder = new StringBuilder(LINE_EST);

            int c = 0;
            while (c > -1) {
                c = in.read();

                if (c < 0 || c == '\n') {
                    final KeyList keys = new KeyList(builder.toString());

                    if (keys.size() > 0) {
                        select(keys);
                    }

                    builder.setLength(0);

                } else if (builder.length() < LINE_LIMIT) {
                    builder.append((char) c);
                }
            }

        }
    }


    private void select(KeyList keys) {
        for(int e = 0; e<list.sizeAll(); e++) {
            if (list.getFromAll(e).getKeys().fits(keys)) {
                list.getFromAll(e).select();
                break;
            }
        }
    }
}
