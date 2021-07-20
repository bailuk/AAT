package ch.bailu.aat.util;

import java.io.IOException;
import java.io.OutputStreamWriter;

import ch.bailu.aat_lib.xml.parser.util.Stream;
import ch.bailu.foc.Foc;


public abstract class TextBackup {

    public abstract void write(String t) throws IOException;
    public abstract String read() throws IOException;

/*
    public static final TextBackup NULL= new TextBackup() {

        @Override
        public void write(String t) throws IOException {}

        @Override
        public String read() throws IOException {
            return "";
        }

    };
  */

    public static void write(Foc file, String text) throws IOException{
        TextBackup b = new TextEditFileBackup(file);
        b.write(text);
    }



    public static String read(Foc file) throws IOException {
        TextBackup b = new TextEditFileBackup(file);
        return b.read();
    }



    public static class TextEditFileBackup extends TextBackup {
        public static final int MAX_FILE_SIZE=200;

        private final Foc file;

        public TextEditFileBackup(Foc f) {
            file = f;

        }



        public void write(String text) throws IOException {
            OutputStreamWriter writer = new OutputStreamWriter(file.openW());

            writer.write(text);
            writer.close();
        }




        public String read() throws IOException {
            StringBuilder buffer = new StringBuilder();
            readToBuffer(buffer);

            return buffer.toString();
        }



        private void readToBuffer(StringBuilder buffer) throws IOException  {
            Stream stream = new Stream(file);

            int count = MAX_FILE_SIZE;

            while(count > -1) {
                count--;

                stream.read();
                if (stream.haveEOF()) {
                    break;

                } else {
                    buffer.append((char)stream.get());

                }
            }
            stream.close();
        }
    }
}
