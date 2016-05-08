package ch.bailu.aat.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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



    public static void importFile(Context context, Intent intent) {
        InputStream is = null;
        FileOutputStream os = null;
        
        try {
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                final Uri uri = intent.getData();
                final File file =  fileFromIntent(context, intent);

                is = context.getContentResolver().openInputStream(uri);
                os = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    os.write(buffer, 0, count);
                }
                os.close();
                is.close();
            }        
        } catch (Exception e) {
            AppLog.e(context, e);

            if (is != null) {
                try {
                    is.close();
                } catch (Exception e1) {}
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e1) {}
            }
        }
    }


    public static File fileFromIntent(Context context, Intent intent) throws IOException {
        File ret;
        final File dir = AppDirectory.getDataDirectory(context, AppDirectory.DIR_IMPORT);
        String name = fileNameFromIntent(context, intent);

        if (name == null) {
            ret = AppDirectory.generateUniqueFilePath(dir, AppDirectory.generateDatePrefix(), "_imp.gpx");
        } else {
            ret = new File(dir, name);  // use original name if possible
            if (ret.exists()) {
                ret = AppDirectory.generateUniqueFilePath(dir, "imp", "_"+ name);
            }
        }

        return ret;
    }


    public static String fileNameFromIntent(Context context, Intent intent) {

        Uri uri = intent.getData();
        String scheme = uri.getScheme();
        String name = null;

        if (scheme.equals("file")) {
            List<String> pathSegments = uri.getPathSegments();
            if (pathSegments.size() > 0) {
                name = pathSegments.get(pathSegments.size() - 1);
            }
        } else if (scheme.equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, new String[] {
                    MediaStore.MediaColumns.DISPLAY_NAME
            }, null, null, null);
            cursor.moveToFirst();
            int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (nameIndex >= 0) {
                name = cursor.getString(nameIndex);
            }
        } 

        return name;
    }
}
