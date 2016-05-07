package ch.bailu.aat.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
    
    
    public static void send(Context context, File file) {
        final Uri attachement = Uri.fromFile(file);
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, file.getName());
        emailIntent.putExtra(Intent.EXTRA_TEXT, file.getAbsolutePath());
        emailIntent.setType("application/gpx+xml");
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachement);
    
        context.startActivity(Intent.createChooser(emailIntent , file.getName()));
    }
}
