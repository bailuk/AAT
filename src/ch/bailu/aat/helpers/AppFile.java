package ch.bailu.aat.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class AppFile {
    public static final File NULL_FILE = new File("/dev/null");;

    public static String contentToString(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);

        try {
            FileChannel channel = stream.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            
            return Charset.defaultCharset().decode(map).toString();

        } finally {
            stream.close();

        }
    }
}
