package ch.bailu.aat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ch.bailu.simpleio.io.FileAccess;
import ch.bailu.simpleio.io.Stream;


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

    public static void write(File file, String text) throws IOException{
        TextBackup b = new TextEditFileBackup(file);
        b.write(text);
    }
    
    
    
    public static String read(File file) throws IOException {
        TextBackup b = new TextEditFileBackup(file);
        return b.read();
    }
    
    
    
    public static class TextEditFileBackup extends TextBackup {
        public static final int MAX_FILE_SIZE=200;

        private final File file;

        public TextEditFileBackup(File f) throws IOException  {
            file = f;
            if (file.exists()==false) {
                file.createNewFile();
            }

        }



        public void write(String text) throws IOException {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));

            writer.write(text);
            writer.close();
        }




        public String read() throws IOException {
            StringBuilder buffer = new StringBuilder();
            readToBuffer(buffer);
            
            return buffer.toString();
        }



        private void readToBuffer(StringBuilder buffer) throws IOException  {
            Stream stream = new Stream(new FileAccess(file));

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
