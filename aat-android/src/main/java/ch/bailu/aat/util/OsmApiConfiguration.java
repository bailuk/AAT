package ch.bailu.aat.util;

import java.io.UnsupportedEncodingException;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;


public abstract class OsmApiConfiguration {

        public final static int NAME_MAX=15;
        public final static int NAME_MIN=2;


        public abstract String getApiName();
        public abstract String getUrl(String query) throws UnsupportedEncodingException;
        public abstract String getUrlStart();
        public abstract Foc getBaseDirectory();
        public abstract String getFileExtension();
        public abstract Exception getException();

        public abstract String getUrlPreview(String s);
        public abstract void startTask(ServiceContext scontext);


        public Foc getResultFile() {
            return getBaseDirectory().child("result"+ getFileExtension());
        }


        public Foc getQueryFile() {
            return getBaseDirectory().child("query.txt");
        }

        public static String getFilePrefix(String query) {
            final StringBuilder name= new StringBuilder();

            for (int i=0; i<query.length() && name.length()<NAME_MAX; i++) {
                appendToName(query.charAt(i), name);
            }

            if (name.length()<NAME_MIN) {
                name.append(AppDirectory.generateDatePrefix());
            }

            return name.toString();
        }


        private static void appendToName(char c, StringBuilder name) {
            if (Character.isLetter(c)) {
                name.append(c);
            }
        }

    final public boolean isTaskRunning(ServiceContext scontext) {
        final boolean[] running = {false};

        new InsideContext(scontext) {
            @Override
            public void run() {
                BackgroundService background = scontext.getBackgroundService();
                running[0] = background.findTask(getResultFile()) != null;
            }
        };
        return running[0];
    }

    final public void stopTask(ServiceContext scontext) {
        new InsideContext(scontext) {
            @Override
            public void run() {
                BackgroundService background = scontext.getBackgroundService();
                BackgroundTask task = background.findTask(getResultFile());
                if (task != null) task.stopProcessing();
            }
        };
    }

}


