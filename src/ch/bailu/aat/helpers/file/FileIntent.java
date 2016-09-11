package ch.bailu.aat.helpers.file;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FileIntent {
 
    private final File file;
    private final Intent intent;
    
    public FileIntent (File f, Intent i) {
        file = f;
        intent = i;
    }
    
    
    public FileIntent(File f) {
        file = f;
        intent = new Intent();
    }
    
    public static final int PICK_RESULT=1;
    
    public void pick(Activity context) {
        if (file.exists() && file.isDirectory()) {
            intent.setData(getFileUri());
            context.startActivityForResult(Intent.createChooser(intent, file.getName()), PICK_RESULT);    
        }
    }

    
    public void view(Context context) {
        if (file.exists()) {
            
            
            intent.setAction(Intent.ACTION_VIEW);
            
            if (file.isDirectory()) {
                intent.setData(getFileUri());
            } else {
                intent.setData(getContentUri());
            }
        }
        context.startActivity(Intent.createChooser(intent, file.getName()));
    }
    
    
    private Uri getFileUri() {
        return Uri.fromFile(file);
    }
    
    
    private Uri getContentUri() {
        return Uri.parse("content://ch.bailu.aat.gpx" + file.getAbsolutePath());
    }

    
   public void send(Context context) {
        /**
         * This is the correct implementation for sending one file as an e-mail attachment.
         * It does, however, not work with private files.
         * 
         */
        //final Uri uri = Uri.fromFile(file);
        final Uri uri = getContentUri(); 
        
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, file.getName());
        intent.putExtra(Intent.EXTRA_TEXT, file.getAbsolutePath());
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        setType(); // only works with type set (gmail and android mail)
        
        context.startActivity(Intent.createChooser(intent , file.getName()));
    }


    private void setType() {
        String type = mimeTypeFromFileName(file.toString());
        
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
