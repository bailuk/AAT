package ch.bailu.aat.util.fs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import ch.bailu.util_java.foc.Foc;

public class FileIntent {


    public static void browse(Context context, Intent intent, Uri uri) {
        final String name = uri.getLastPathSegment();

        if (name != null) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_SUBJECT, name);
            intent.putExtra(Intent.EXTRA_TEXT, name);
            intent.setDataAndType(uri, "resource/folder");
            context.startActivity(Intent.createChooser(intent, name));
        }
    }


    public static void view(Context context, Intent intent, Foc file) {
        if (file.canRead()) {
            
            intent.setAction(Intent.ACTION_VIEW);
            
            if (file.isDir() || file.toString().startsWith("content:/")) {
                intent.setData(Uri.parse(file.toString()));
            } else {
                intent.setData(toContentUri(file));
            }
            context.startActivity(Intent.createChooser(intent, file.getName()));
        }
    }


    public static void view(Context context, Intent intent, Uri uri) {
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        context.startActivity(Intent.createChooser(intent, uri.getLastPathSegment()));
    }


    public static Uri toUri(File file) {
        return Uri.fromFile(file);
    }
    
    
    public static Uri toContentUri(Foc file) {
        return Uri.parse("content://ch.bailu.aat.gpx" + file.toString());
    }

    
   public static void send(Context context, Intent intent, Foc file) {
        /*
          This is the correct implementation for sending one file as an e-mail attachment.
          It does, however, not work with private files.

         */
        //final Uri uri = Uri.fromFile(file);
        final Uri uri = toContentUri(file);
        
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, file.getName());
        intent.putExtra(Intent.EXTRA_TEXT, file.toString());
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        setType(intent, file); // only works with type setCopy (gmail and android mail)
        
        context.startActivity(Intent.createChooser(intent , file.getName()));
    }


    public static void send(Context context, Intent intent, Uri uri) {

        final String name = uri.getLastPathSegment();

        if (name != null) {
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, name);
            intent.putExtra(Intent.EXTRA_TEXT, uri.toString());
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            setType(intent, name); // only works with type setCopy (gmail and android mail)

            context.startActivity(Intent.createChooser(intent, name));
        }
    }


    private static void setType(Intent intent, Foc file) {
        String type = mimeTypeFromFileName(file.getName());
        
        if (type != null) {
            intent.setType(type);
        }
    }

    private static void setType(Intent intent, String name) {
        String type = mimeTypeFromFileName(name);

        if (type != null) {
            intent.setType(type);
        }
    }
    
    public static String mimeTypeFromFileName(String name) {
        if (name.endsWith(".gpx")) return "application/gpx+xml";
        else if (name.endsWith(".osm")) return "application/xml";
        return null;
    }
}
