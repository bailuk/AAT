package ch.bailu.util_java.parser.scanner;

import java.io.IOException;
import java.io.Reader;

public class SimpleStringReader extends Reader {

    private String string = new String();
    private int index = 0;

    public void setString(String s) throws IOException {
        string = s;
        reset();
    }


    @Override
    public int read() throws IOException {
        if (index < string.length()) {
            int c = string.charAt(index);
            index ++;
            return c;
        }
        return -1;
    }


    @Override
    public int read(char[] cbuf) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }



    @Override
    public long skip(long n) throws IOException {
        if (index + n >= string.length()) {
            n = string.length()- index;
        }
        index += n;

        return n;
    }

    @Override
    public boolean ready() throws IOException {
        return true;
    }


    @Override
    public void reset() throws IOException {
        index = 0;
    }





    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (index >= string.length()) return -1;

        int c = 0;
        int out = off;

        while (c < len && out < cbuf.length ) {
            cbuf[off] = string.charAt(index);

            if (index + 1 < string.length()) {
                index ++;
                c++;
                out++;
            } else {
                break;
            }
        }
        return c;
    }


    @Override
    public void close() throws IOException {

    }
}

