package ch.bailu.aat.util.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;

import java.io.Closeable;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.Timer;


public class AppLog implements Closeable {
    private final static String UNKNOWN = "";
    
    public final static String NAME_SPACE= AppBroadcaster.NAME_SPACE;
    public final static String LOG_INFO = NAME_SPACE + "LOG_INFO";
    public final static String LOG_E = NAME_SPACE + "LOG_ERROR";

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private final static String EXTRA_SOURCE = "TITLE";
    

    private abstract static class Log extends BroadcastReceiver implements Closeable {

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

    
    
    private static class LogError extends Log {
        private final AlertDialog.Builder alertbox;
        
        public LogError(Context c) {
            super(c, LOG_E);
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
    


    private final Log error;

    public AppLog(Context context) {
        error = new LogError(context);
    }
    
    public static void i(Context c, String m) {
        sendBroadcast(LOG_INFO, c, toSaveString(m));
    }

    private static void i(Object o, String m) {
        i(o.getClass().getSimpleName(), m);
    }
    
    private static void i(String a, String b) {
        android.util.Log.i(toSaveString(a), toSaveString(b));
    }

    
    public static void e(Context c, Throwable e) {
        e(c, toStringAndPrintStackTrace(e));
    }


    public static void e(Context c, Object o, Throwable e) {
        e(c, o.getClass().getSimpleName(), toStringAndPrintStackTrace(e));
    }

    
    
    private static void e(Object o, Throwable e) {
        e(o, toStringAndPrintStackTrace(e));
    }


    private static void e(Object o, String m) {
        e(o.getClass().getSimpleName(), m);
    }

    
    private static void e(String a, Throwable e) {
        e(a, toStringAndPrintStackTrace(e));
    }
    
    
    private static void e(String a, String b) {
        android.util.Log.e(toSaveString(a), toSaveString(b));

    }
  
    
    public static void e(Context c, String m) {
        sendBroadcast(LOG_E, c, toSaveString(m));
    }

    
    public static void e(Context c, String a, String b) {
        sendBroadcast(LOG_E, c, toSaveString(a), toSaveString(b));
    }
    
    
    public static void d(Object o, String m) {
    	d(o.getClass().getSimpleName(), m);
    }
    
    public static void d(String a, String b) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(toSaveString(a), toSaveString(b));
        }
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


    private static String toSaveString(String s) {
        if (s == null) return UNKNOWN;
        return s;
    }

    private static String toStringAndPrintStackTrace(Throwable e) {
        if (e == null) {
            return UNKNOWN;
        } else {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }

            if (e.getMessage() == null) {
                return e.getClass().getSimpleName();
            } else {
                return e.getClass().getSimpleName() + ": " + e.getMessage();
            }
        }
    }
    
    
    @Override
    public void close() {
        error.close();
    }
}
