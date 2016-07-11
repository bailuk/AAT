package ch.bailu.aat.helpers;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


    public static void copy(Context context, Uri uri, File target) throws Exception {
        InputStream is = null;
        OutputStream os = null;

        try {

            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(target);
            copy(is, os);
            os.close();
            is.close();
        } catch (Exception e) {
            closeStream(is);
            closeStream(os);
            throw e;
        }
    }

    public static void copy(File source, File target) throws Exception {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(target);
            copy(is, os);
            os.close();
            is.close();
        } catch (Exception e) {
            closeStream(is);
            closeStream(os);
            throw e;
        }
    }

    private static void closeStream(Closeable c) {
        try {
            if (c != null) c.close();
        } catch (IOException e) {}
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[4096];
        int count;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
        }
    }

    
    public static void copyTo(Context context, File file) throws IOException {
        new AppSelectDirectoryDialog(context, file);
    }

    public static void copyTo(Context context, File file, File targetDir) throws Exception {
        final File target = new File(targetDir, file.getName());

        if (target.exists()) {
            AppLog.e(context, target.toString() + " allready exists.*");
        } else {
            copy(file, target);
            AppLog.i(context, target.getAbsolutePath());
        }

    }
}
