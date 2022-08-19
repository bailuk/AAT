package ch.bailu.aat_lib.search.poi;

import java.io.UnsupportedEncodingException;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface;
import ch.bailu.aat_lib.service.background.BackgroundTask;
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
        public abstract void startTask(AppContext scontext);


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

    final public boolean isTaskRunning(ServicesInterface scontext) {
        final boolean[] running = {false};

        new InsideContext(scontext) {
            @Override
            public void run() {
                BackgroundServiceInterface background = scontext.getBackgroundService();
                running[0] = background.findTask(getResultFile()) != null;
            }
        };
        return running[0];
    }

    final public void stopTask(ServicesInterface scontext) {
        new InsideContext(scontext) {
            @Override
            public void run() {
                BackgroundServiceInterface background = scontext.getBackgroundService();
                BackgroundTask task = background.findTask(getResultFile());
                if (task != null) task.stopProcessing();
            }
        };
    }
}
