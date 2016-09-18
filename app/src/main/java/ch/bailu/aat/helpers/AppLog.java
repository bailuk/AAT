package ch.bailu.aat.helpers;

import java.io.Closeable;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;


public class AppLog implements Closeable {
    private final static String UNKNOWN = "";
    
    private final static String NAME_SPACE=AppBroadcaster.NAME_SPACE;
    private final static String EXTRA_MESSAGE = "MESSAGE";
    private final static String EXTRA_SOURCE = "TITLE";
    

    private abstract class Log extends BroadcastReceiver implements Closeable {

        private final Context context;
        
        public Log(Context c, String action) {
            context = c;
            AppBroadcaster.register(context, this, action);
        }
        
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            String title = intent.getStringExtra(EXTRA_SOURCE);
            log(title, message);
            
        }

        @Override
        public void close() {
            context.unregisterReceiver(this);
        }
        
        public abstract void log(String t, String m);
    }

    
    
    private class LogError extends Log {
        public final static String ACTION = NAME_SPACE + "LOG_ERROR";
        private final AlertDialog.Builder alertbox;
        
        public LogError(Context c) {
            super(c, ACTION);
            alertbox = new AlertDialog.Builder(c);
            alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {}
            });
        }

        
        @Override
        public void log(String t, String m) {
            if (Looper.myLooper()==null) {
                e(t, m); 
            } else {
                alertbox.setTitle(t);
                alertbox.setMessage(m);
                alertbox.show();
            }
        }
    }
    
    
    private class LogInfo extends Log {
        public final static String ACTION = NAME_SPACE + "LOG_INFO";
        private final Toast toast;
        
        public LogInfo(Context c) {
            super(c, ACTION);
            toast =  Toast.makeText(c, getClass().getSimpleName(), Toast.LENGTH_LONG);
        }


        @Override
        public void log(String t, String m) {
            toast.setText(m);
            toast.show();
        }
    }

    
    
    private final Log info, error;

    public AppLog(Context context) {
        info = new LogInfo(context);
        error = new LogError(context);
    }
    
    public static void i(Context c, String m) {
        sendBroadcast(LogInfo.ACTION, c, safeString(m));
    }

    public static void i(Object o, String m) {
        i(o.getClass().getSimpleName(), m);
    }
    
    public static void i(String a, String b) {
        android.util.Log.i(safeString(a), safeString(b));
    }

    
    public static void e(Context c, Throwable e) {
        e(c,safeString(e));
        e.printStackTrace();
    }


    public static void e(Context c, Object o, Throwable e) {
        e(c, o.getClass().getSimpleName(), safeString(e));
        e.printStackTrace();
    }

    
    
    public static void e(Object o, Throwable e) {
        e(o,safeString(e));
        e.printStackTrace();
    }


    public static void e(Object o, String m) {
        e(o.getClass().getSimpleName(), m);
    }

    
    public static void e(String a, Throwable e) {
        e(a, e.getMessage());
        e.printStackTrace();
    }
    
    
    public static void e(String a, String b) {
        android.util.Log.e(safeString(a), safeString(b));
    }
  
    
    public static void e(Context c, String m) {
        sendBroadcast(LogError.ACTION, c, safeString(m));
    }

    
    public static void e(Context c, String a, String b) {
        sendBroadcast(LogError.ACTION, c, safeString(a), safeString(b));
    }
    
    
    public static void d(Object o, String m) {
    	d(o.getClass().getSimpleName(), m);
    }
    
    public static void d(String a, String b) {
        android.util.Log.d(safeString(a), safeString(b));
    }
    

    private static void sendBroadcast(String i, Context c, String m) {
        sendBroadcast(i,c,UNKNOWN, m);
    }
    
    
    private static void sendBroadcast(String i, Context c, String s, String m) {
        Intent intent = new Intent(i);
        intent.putExtra(EXTRA_MESSAGE, m);
        intent.putExtra(EXTRA_SOURCE, s);
        c.sendBroadcast(intent);
    }


    private static String safeString(String s) {
        if (s == null) return UNKNOWN;
        return s;
    }

    private static String safeString(Throwable e) {
        if (e == null) 
            return UNKNOWN;
        else if (e.getMessage() == null) 
            return e.getClass().getSimpleName();
        else 
            return e.getClass().getSimpleName() +": " + e.getMessage();
    }
    
    
    @Override
    public void close() {
        info.close();
        error.close();
    }
}
